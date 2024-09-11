package com.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Email.JavaEmailSender;
import com.Email.PdfService;
import com.Model.CartItem;
import com.Model.Product;
import com.Model.User;
import com.Repository.UserRepository;

@RestController
public class EmailController {
	@Autowired
	private JavaEmailSender emailSender;
	@Autowired
	private PdfService  pdfService;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@PostMapping(value = "sendEmail/{userId}")
	public ResponseEntity<String> Sendemail(@PathVariable(name = "userId") Long UserId) throws Exception{
		System.out.println("Running Email");
		User user = userRepository.findById(UserId).orElseThrow(()-> new Exception("User Not Found"));
		List<CartItem> userproducts = user.getUserCartList().getCartItems();
		byte[] invoice =pdfService.generateInvoicePdf(user.getUserName(), user.getUserCartList());
		try {
			emailSender.sendInvoiceEmail(user.getEmail(), "INVOICE","Thanks For Shopping", invoice);
			return ResponseEntity.status(HttpStatus.OK).body("Email sent");
		}catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sent Failed"+e);
		}
		
		
		
	}

}
