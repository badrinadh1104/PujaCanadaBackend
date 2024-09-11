package com.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.Email.JavaEmailSender;
import com.Email.PdfService;
import com.Model.CartList;
import com.Model.PujaUserOrder;
import com.Model.User;
import com.Repository.PujaUserOrderRepository;
import com.Repository.UserRepository;

@Service
public class PujaOrderService {
	@Autowired
	private PujaUserOrderRepository pujaUserOrderRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JavaEmailSender emailSender;

	@Autowired
	private PdfService pdfService;

	public List<PujaUserOrder> getAllOrders() {
		return pujaUserOrderRepository.findAll();
	}

	public String sendInvoice(Long orderId) {
	    PujaUserOrder pujaUserOrder = pujaUserOrderRepository.findById(orderId)
	            .orElseThrow(() -> new NotFoundException("Order does not exist with Order ID " + orderId));
	    try {
	        CartList cartList = new CartList();
	        cartList.setCartItems(pujaUserOrder.getOrderedProducts());
	        cartList.setUserCartListPujas(pujaUserOrder.getPujaOrders());
	        byte[] invoice = pdfService.generateInvoicePdf(pujaUserOrder.getUser().getUserName(), cartList);
	        emailSender.sendEmail(pujaUserOrder.getUser(), cartList, invoice);
//			emailSender.sendInvoiceEmail(pujaUserOrder.getUser().getEmail(), "INVOICE", "", invoice);
	        return "Invoice sent successfully";
	    } catch (Exception emailException) {
	        System.out.println("emailError: " + emailException);
	        // Optionally log the stack trace for debugging purposes
	        emailException.printStackTrace();
	        return "Failed to send invoice: " + emailException.getMessage();
	    }
	}


}
