package com.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Model.Earnings;
import com.Service.EarningsService;
import com.Service.PriestService;

@RestController
@CrossOrigin
@RequestMapping(value = "Earnings")
public class EarningsController {
	
	@Autowired
	private PriestService priestService;
	
	@Autowired
	private EarningsService earningsService;
	
	@GetMapping(value = "PriestEarnings/{priestId}")
	public List<Earnings> PriestEarnings(@PathVariable(name = "priestId") Long priestId) throws Exception{
		return priestService.PriestEarnings(priestId);
	}
	
	@GetMapping(value = "getAllEarnings")
	public List<Earnings> getAllEarnings(){
		return earningsService.getAllEarnings();
	}

}
