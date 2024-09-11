package com.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Email.JavaEmailSender;
import com.Email.PdfService;
import com.Enums.PaymentStatus;
import com.Exceptions.PaymentFailed;
import com.Model.CartItem;
import com.Model.CartList;
import com.Model.Payment;
import com.Model.Product;
import com.Model.PujaAppointment;
import com.Model.PujaUserOrder;
import com.Model.User;
import com.Repository.PaymentRepository;
import com.Repository.ProductRepository;
import com.Repository.PujaUserOrderRepository;
import com.Repository.UserRepository;
import com.stripe.model.Charge;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PujaUserOrderRepository orderRepository;

	@Autowired
	private StripeClient stripeClient;

	@Autowired
	private JavaEmailSender emailSender;
	
	@Autowired
	private PdfService pdfService;
	
	@Autowired
	private ProductRepository productRepository;

	public ResponseEntity<Map<String, Object>> chargeCard(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		try {
			String token = request.getHeader("token");
			Long userId = Long.parseLong(request.getHeader("userId"));
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new Exception("User Not Found With user Id" + userId));
			if (!user.getUserCartList().getCartItems().isEmpty() || !user.getPujaAppointments().isEmpty()) {
				float amount = CalculateTotalPay(user.getUserCartList());
				Charge charge = stripeClient.chargeCreditCard(token, amount);

				Payment payment = new Payment();
				Date date = new Date();
				payment.setPaymentDate(date);
				payment.setPaymentid(charge.getId());
				payment.setPujaUser(user);
				payment.setPaymentStatus(charge.getStatus());
				payment.setPaymentType("card");
				payment.setPaymentAmount(amount);

				PujaUserOrder order = new PujaUserOrder();
				order.setTotal(amount);
				order.setUser(user);
				order.setPaymentStatus(true);
				for (PujaAppointment appointment : user.getUserCartList().getUserCartListPujas()) {
					appointment.setConfirmed(true);
					System.out.println("Confirmed");
				}
				userRepository.save(user);
				for (PujaAppointment appointment : user.getUserCartList().getUserCartListPujas()) {
					order.AddPujatoOrder(appointment);
				}
				for (CartItem cartItem : user.getUserCartList().getCartItems()) {
					order.AddtouserOrderedProducts(cartItem);
					Product prod = cartItem.getProduct();
					prod.setQuantity(prod.getQuantity()-cartItem.getQuantity());
					productRepository.save(prod);
				}
//				byte[] invoice = pdfService.generateInvoicePdf(user.getUserName(), user.getUserCartList());
//				emailSender.sendInvoiceEmail(user.getEmail(), "INVOICE", "Thanks For Shopping", invoice);
				try {
	                byte[] invoice = pdfService.generateInvoicePdf(user.getUserName(), user.getUserCartList());
	                emailSender.sendInvoiceEmail(user.getEmail(), "INVOICE", "Thanks For Shopping", invoice);
	            } catch (Exception emailException) {
	                response.put("emailError", "Failed to send email: " + emailException.getMessage());
	                // Optionally log the stack trace for debugging purposes
	                emailException.printStackTrace();
	            }
				orderRepository.save(order);
				payment.setUserOrder(order);
				paymentRepository.save(payment);
				response.put("success", true);
				response.put("chargeId", charge.getId());
				response.put("status", charge.getStatus());
				userService.clearUserCartList(userId);
				return ResponseEntity.ok(response);
			} else {
				throw new Exception("User Cart is Empty");
			}

		} catch (Exception e) {
			// TODO: handle exception
			response.put("success", false);
			response.put("error", "An error occurred while processing the payment.");
			return ResponseEntity.badRequest().body(response);
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

	public List<Payment> getAllPayments() {
		return paymentRepository.findAll();
	}

}
