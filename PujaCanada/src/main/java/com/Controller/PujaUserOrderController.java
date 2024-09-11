package com.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Model.PujaUserOrder;
import com.Service.PujaOrderService;

@RestController
@CrossOrigin
@RequestMapping(value = "UserOrder")
public class PujaUserOrderController {

	
	@Autowired
	private PujaOrderService orderService;
	
	
	@GetMapping(value = "getAllOrders")
	public List<PujaUserOrder> getAllOrders(){
		return orderService.getAllOrders();
	}
	
	@GetMapping(value = "sendEmail/{orderId}")
	public String sendEmail(@PathVariable(name = "orderId") Long orderId) {
		return orderService.sendInvoice(orderId);
	}
}
