package com.Service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Model.Earnings;
import com.Model.Priest;
import com.Model.Puja;
import com.Model.PujaAppointment;
import com.Model.User;
import com.Repository.EarningsRepository;
import com.Repository.PriestRepository;
import com.Repository.PujaAppointmentRepository;
import com.Repository.PujaRepository;
import com.Repository.UserRepository;

@Service
public class PriestService {
	
	@Autowired
	private PriestRepository priestRepository;
	
	@Autowired
	private PujaRepository pujaRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PujaAppointmentRepository pujaAppointmentRepository;
	
	@Autowired
	private EarningsRepository earningsRepository;
	
//	To Add the Priest 
	public Priest AddPriest(Priest priest) throws Exception {
		User user = userRepository.findByEmail(priest.getUser().getEmail());
		if(user == null) {
			return priestRepository.save(priest);
		}else {
			throw new Exception ("Priest Already Exist ");
		}
	}
	
	
	
//	Add Puja To the Priest 
	
	public Priest AddPujaToPriest(Long priestId,Long pujaId) throws Exception {
		Priest priest = priestRepository.findById(priestId).orElseThrow(()-> new Exception("Priest Not Found With ID "+priestId));
		Puja puja = pujaRepository.findById(pujaId).orElseThrow(()-> new Exception("Puja Not Found With ID "+pujaId));
		priest.AddpujastoPriest(puja);
		return priestRepository.save(priest);
	}
	
//	to Get All the Priest Appointments
	
	public List<PujaAppointment> PriestAppointmentsToDo(Long priestId) throws Exception{
		Priest priest = priestRepository.findById(priestId).orElseThrow(()-> new Exception("Priest Not Found With ID "+priestId));
		return priest.getPujasTodo();
	}
	
//	Get All the Pujas Offered By the Priest 
	
	public Set<Puja> GetAllPriestPujas(Long priestId) throws Exception{
		Priest priest = priestRepository.findById(priestId).orElseThrow(()-> new Exception("Priest Not Found With ID "+priestId));
		return priest.getPriestPujas();
	}
	
//	Get All the Earnings Of Priest
	
	public List<Earnings> PriestEarnings(Long priestId) throws Exception{
		Priest priest = priestRepository.findById(priestId).orElseThrow(()-> new Exception("Priest Not Found With ID "+priestId));
		return priest.getPriestEarnings();
	}
	
//	Assign the PujaAppointment to priest
	public List<PujaAppointment> AssignPujaToPriest(Long priestId,Long appointmentId) throws Exception{
		Priest priest = priestRepository.findById(priestId).orElseThrow(()-> new Exception("Priest Not Found With ID "+priestId));
		PujaAppointment appointment = pujaAppointmentRepository.findById(appointmentId).orElseThrow(()-> new Exception("PujaAppointmnet Not Found With ID "+appointmentId));
		if(! appointment.isConfirmed()) {
			throw new Exception("Appointment is still not Confirmed By the User "+appointment.getUserPujaAppointment().getUserName());
		}
		if(priest.isAvailable()) {
			if(priest.getPujasTodo().size() <= 3) {
				if(priest.getPriestPujas().contains(appointment.getPuja())) {
					priest.PriestpujaTodo(appointment);
					appointment.setPujaPriest(priest);
					priestRepository.save(priest);
					
					pujaAppointmentRepository.save(appointment);
					return priestRepository.findById(priestId).get().getPujasTodo();
				}else {
					throw new Exception("Priest Cannot Perform "+appointment.getPuja().getName()+" Puja");
				}
			}else {
				throw new Exception ("Priest Puja's Limit is Reached");
			}
		}else {
			throw new Exception("Currently the Priest is Un-Available to Assign");
		}
	}
	
	
//	get All Priest
	
	public List<Priest> getAllPriest(){
		return priestRepository.findAll();
	}
	
	
//	update priestAvailability;
	public Priest UpdatePriestAvailability(Long priestId) throws Exception {
		Priest priest = priestRepository.findById(priestId).orElseThrow(()-> new Exception("Priest Not Found With ID "+priestId));
		
//		if(priest.isAvailable()) {
//			priest.setAvailable(false);
//			return priestRepository.save(priest);
//		}else {
//			priest.setAvailable(true);
//			return priestRepository.save(priest);
//		}
		priest.setAvailable(!priest.isAvailable());
		return priestRepository.save(priest);
	}
	
	
	
//	to update the appointment as Completed
	
	public Priest updateAppointmentCompleted(Long priestId,Long appointmentId) throws Exception {
		Priest priest = priestRepository.findById(priestId).orElseThrow(()-> new Exception("Priest Not Found With ID "+priestId));
		PujaAppointment appointment = pujaAppointmentRepository.findById(appointmentId).orElseThrow(()-> new Exception("PujaAppointmnet Not Found With ID "+appointmentId));
		priest.getPujasTodo().remove(appointment);
		appointment.setCompletedStatus(true);
		Earnings earnings  = new Earnings();
		earnings.setAmount(appointment.getPujaFee()*0.6f);
		earnings.setPriest(priest);
		earnings.setPujaAppointment(appointment);
		pujaAppointmentRepository.save(appointment);
		earningsRepository.save(earnings);
		return priestRepository.save(priest);
	}
}
