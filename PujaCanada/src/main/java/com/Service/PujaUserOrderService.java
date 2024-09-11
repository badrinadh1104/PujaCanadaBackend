package com.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Model.PujaUserOrder;
import com.Repository.PujaUserOrderRepository;

@Service
public class PujaUserOrderService {
	
	@Autowired
	private PujaUserOrderRepository orderRepository;
	
	public List<PujaUserOrder> getAllOrders() {
		return orderRepository.findAll();
	}
	
//	public PujaUserOrder 
	
	

}
