package com.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Model.Earnings;
import com.Model.Priest;
import com.Repository.EarningsRepository;
import com.Repository.PriestRepository;

@Service
public class EarningsService {

	@Autowired
	private PriestRepository priestRepository;
	
	@Autowired
	private EarningsRepository  earningsRepository;

	public List<Earnings> getAllPriestEarnings(Long priestId) throws Exception {
		Priest priest = priestRepository.findById(priestId)
				.orElseThrow(() -> new Exception("Priest Not Found With ID " + priestId));
		return priest.getPriestEarnings();
	}

	public List<Earnings> getAllEarnings() {
		return earningsRepository.findAll();
	}

}
