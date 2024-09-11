package com.Controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Model.Earnings;
import com.Model.Priest;
import com.Model.Puja;
import com.Model.PujaAppointment;
import com.Service.PriestService;

@RestController
@CrossOrigin
@RequestMapping("Priest")
public class PriestController {
	
	@Autowired
	private PriestService priestService;
	
	
	@PostMapping(value = "Addpriest")
	public Priest AddPriest(@RequestBody Priest priest) throws Exception {
		return priestService.AddPriest(priest);
	}
	
	@PutMapping(value = "AddPujaToPriest/{priestId}/{pujaId}")
	public Priest AddPujaToPriest(@PathVariable(name = "priestId") Long priestId,@PathVariable(name = "pujaId")Long pujaId) throws Exception {
		return priestService.AddPujaToPriest(priestId, pujaId);
	}
	
	@GetMapping(value = "PriestAppointmentsToDo/{priestId}")
	public List<PujaAppointment> PriestAppointmentsToDo(@PathVariable(name = "priestId") Long priestId) throws Exception{
		return priestService.PriestAppointmentsToDo(priestId);
	}
	
	@GetMapping(value = "GetAllPriestPujas/{priestId}")
	public Set<Puja> GetAllPriestPujas(@PathVariable(name = "priestId") Long priestId) throws Exception{
		return priestService.GetAllPriestPujas(priestId);
	}
	

	
	@PutMapping(value = "AssignPujaToPriest/{priestId}/{appointmentId}")
	public List<PujaAppointment> AssignPujaToPriest(@PathVariable(name = "priestId") Long priestId,@PathVariable(name = "appointmentId") Long appointmentId) throws Exception{
		return priestService.AssignPujaToPriest(priestId, appointmentId);
		
	}
	@GetMapping(value ="getAllPriest" )
	public List<Priest> getAllPriest(){
		return priestService.getAllPriest();
	}
	
	@PutMapping(value = "UpdatePriestAvailability/{priestId}")
	public Priest UpdatePriestAvailability(@PathVariable(name = "priestId") Long priestId) throws Exception{
		return priestService.UpdatePriestAvailability(priestId);
		
	}
	
	@PutMapping(value = "updateAppointmentCompleted/{priestId}/{appointmentId}")
	public Priest updateAppointmentCompleted(@PathVariable(name = "priestId") Long priestId,@PathVariable(name = "appointmentId") Long appointmentId) throws Exception{
		return priestService.updateAppointmentCompleted(priestId,appointmentId);
		
	}

}
