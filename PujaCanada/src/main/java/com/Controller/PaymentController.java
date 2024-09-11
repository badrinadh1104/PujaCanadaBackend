package com.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Model.Payment;
import com.Service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("Payment")
public class PaymentController {

	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping(value = "ChargeUserCard")
	public ResponseEntity<Map<String, Object>> chargeCard(HttpServletRequest request){
		return paymentService.chargeCard(request);
	}
	
	@GetMapping(value = "AllPayments")
	public List<Payment> getAllPayments(){
		return paymentService.getAllPayments();
	}
}
