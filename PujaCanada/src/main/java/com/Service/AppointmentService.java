package com.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.Model.CartItem;
import com.Model.CartList;
import com.Model.Priest;
import com.Model.Puja;
import com.Model.PujaAppointment;
import com.Model.User;
import com.Repository.CartListRepository;
import com.Repository.PriestRepository;
import com.Repository.PujaAppointmentRepository;
import com.Repository.PujaRepository;
import com.Repository.UserRepository;
import com.dto.PujaAppointmentDto;

@Service
public class AppointmentService {
	@Autowired
	private PujaAppointmentRepository pujaAppointmentRepository;

	@Autowired
	private PujaRepository pujaRepository;

	@Autowired
	private PriestRepository priestRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartListRepository cartListRepository;
	
	
	private PaymentService paymentService;

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

	public User CreateAppointment(PujaAppointmentDto appointmentDto) throws Exception {
		User user = userRepository.findById(appointmentDto.getUserId()).get();
		if (user != null) {
			Puja puja = pujaRepository.findById(appointmentDto.getPujaId()).get();
			if (puja != null) {
				PujaAppointment pujaAppointment = new PujaAppointment();
				pujaAppointment.setUserPujaAppointment(user);
				pujaAppointment.setPuja(puja);
				pujaAppointment.setCompletedStatus(false);
				pujaAppointment.setConfirmed(false);
				pujaAppointment
						.setAppointmentDate(LocalDate.parse(appointmentDto.getAppointmentDate(), DATE_FORMATTER));
				pujaAppointment
						.setAppointmentTime(LocalTime.parse(appointmentDto.getAppointmentTime(), TIME_FORMATTER));
				float discount = pujaAppointment.getPuja().getDiscount() / 100.0f;
				pujaAppointment.setPujaFee(pujaAppointment.getPuja().getPrice() - discount);
				boolean exists = false;
				for (PujaAppointment appointment : user.getUserCartList().getUserCartListPujas()) {
					if (appointment.getPuja().getId().equals(puja.getId()) && !appointment.isCompletedStatus()) {
						exists = true;
						break;
					}

				}
				if (!exists) {
					user.AddAppointment(pujaAppointment);
					user.getUserCartList().AddPujatoCartlistPuja(pujaAppointment);
					float cartListTotal = CalculateTotalPay(user.getUserCartList());
					user.getUserCartList().setCartTotal(cartListTotal);
					cartListRepository.save(user.getUserCartList());
					
					userRepository.save(user);
					return user;
				} else {
					throw new Exception("Already Appointment Exist And Still Pending");
				}

//				AddPujatoUserCartList(user.getId(), pujaAppointment);

			} else {
				throw new Exception("Sorry We Dont Find any puja with pujaID " + appointmentDto.getPujaId());

			}

		} else {
			throw new Exception("Sorry No user Found with UserID " + appointmentDto.getUserId());
		}

	}
	
	public float CalculateTotalPay(CartList cartList) {
		float totalamount = 0;
		if (cartList.getCartItems() != null) {
			for (CartItem cartItem : cartList.getCartItems()) {
				totalamount = totalamount + cartItem.getTotal();
			}
		}
		if (cartList.getUserCartListPujas() != null) {
			for (PujaAppointment appointment : cartList.getUserCartListPujas()) {
				totalamount = totalamount + appointment.getPujaFee();
			}
		}
		return totalamount;
	}

	public User AddPujatoUserCartList(Long userId, PujaAppointment pujaAppointment) {
		User user = userRepository.findById(userId).get();
		if (user != null) {
			if (pujaAppointment != null) {
				user.getUserCartList().AddPujatoCartlistPuja(pujaAppointment);
				cartListRepository.save(user.getUserCartList());
				return userRepository.save(user);
			} else {
				throw new NotFoundException("pujaAppintment does not Exist or NULL");
			}
		} else {
			throw new NotFoundException("User does not Exist with User ID " + userId);
		}
	}

//	to get the history of the appointments for the user .
	public List<PujaAppointment> CompletedUserAppointments(Long UserId) throws Exception {
		User user = userRepository.findById(UserId)
				.orElseThrow(() -> new Exception("User Not Found with Id " + UserId));
		List<PujaAppointment> CompletedAppointments = user.getPujaAppointments().stream()
				.filter(x -> x.isCompletedStatus()).collect(Collectors.toList());
		return CompletedAppointments;
	}

//	to get the user current appointment
	public List<PujaAppointment> PendingUserAppointments(Long UserId) throws Exception {
		User user = userRepository.findById(UserId)
				.orElseThrow(() -> new Exception("User Not Found with Id " + UserId));
		List<PujaAppointment> CompletedAppointments = user.getPujaAppointments().stream()
				.filter(x -> !x.isCompletedStatus()).collect(Collectors.toList());
		return CompletedAppointments;
	}

//	update the user Appointment
	public PujaAppointment UpdatePujaCompleted(Long userId, Long AppointmentId) throws Exception {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new Exception("User Not Found with Id " + userId));
//		Puja puja = pujaRepository.findById(PujaId).orElseThrow(()-> new Exception("Puja Not Exist with Id "+PujaId));
		PujaAppointment pujaAppointment = pujaAppointmentRepository.findById(AppointmentId).get();
//		user.getPujaAppointments().stream().filter(appointment -> appointment.getPuja().getId().equals(PujaId)).forEach(appointmnet -> appointmnet.setCompletedStatus(true));

		user.getPujaAppointments().stream().filter(appointment -> appointment.getId().equals(AppointmentId))
				.forEach(appointment -> appointment.setCompletedStatus(true));
		userRepository.save(user);
		return user.getPujaAppointments().stream().filter(appointment -> appointment.getId().equals(AppointmentId))
				.findFirst().get();
	}

//	to assign the Priest t0 the Appointment
	public PujaAppointment AssignPriestToAppointmnet(Long PriestId, Long AppointmentId) throws Exception {
		Priest priest = priestRepository.findById(PriestId)
				.orElseThrow(() -> new Exception("Priest Not Exist With Id " + PriestId));
		PujaAppointment pujaAppointment = pujaAppointmentRepository.findById(AppointmentId)
				.orElseThrow(() -> new Exception("Appointment with Appointment ID " + AppointmentId));
		pujaAppointment.setPujaPriest(priest);
		pujaAppointment.setConfirmed(true);
		return pujaAppointmentRepository.save(pujaAppointment);
	}

//	to list all the appointments
	public List<PujaAppointment> ShowAllAppointments() {
		return pujaAppointmentRepository.findAll().stream().filter(Appointmnet -> !Appointmnet.isCompletedStatus())
				.collect(Collectors.toList());
	}

	public String ClearAppointments() {
		pujaAppointmentRepository.deleteAll();
		return "deleted";

	}

}
