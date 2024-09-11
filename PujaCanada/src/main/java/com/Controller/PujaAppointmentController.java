package com.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Model.Product;
import com.Model.PujaAppointment;
import com.Model.User;
import com.Service.AppointmentService;
import com.dto.PujaAppointmentDto;

@RestController
@RequestMapping("PujaAppointment")
@CrossOrigin
public class PujaAppointmentController {
	
	@Autowired
	private AppointmentService appointmentService;
	
	@PostMapping(value = "CreateAppointmnet")
	public User CreateAppointment(@RequestBody PujaAppointmentDto appointmentDto) throws Exception {
		return appointmentService.CreateAppointment(appointmentDto);
	}
	
	@GetMapping(value = "CompletedUserAppointments/{userId}")
	public List<PujaAppointment> CompletedUserAppointments(@PathVariable(name = "userId") Long userId) throws Exception{
		return appointmentService.CompletedUserAppointments(userId);
	}
	
	@GetMapping(value = "PendingUserAppointments/{userId}")
	public List<PujaAppointment> PendingUserAppointments(@PathVariable(name = "userId") Long userId) throws Exception{
		return appointmentService.PendingUserAppointments(userId);
	}
	
	@PutMapping(value = "UpdatePujaCompleted/{userId}/{AppointmentId}")
	public PujaAppointment UpdatePujaCompleted(@PathVariable(name = "userId") Long userId ,@PathVariable(name = "AppointmentId")Long AppointmentId) throws Exception {
		return appointmentService.UpdatePujaCompleted(userId, AppointmentId);
	}
	
	@PutMapping(value = "AssignPriestToAppointmnet/{PriestId}/{AppointmentId}")
	public PujaAppointment AssignPriestToAppointmnet(@PathVariable(name = "PriestId") Long PriestId,@PathVariable(name = "AppointmentId") Long AppointmentId) throws Exception {
		return appointmentService.AssignPriestToAppointmnet(PriestId, AppointmentId);
	}
	
	
	
	@GetMapping(value = "ShowAllAppointments")
	public List<PujaAppointment> ShowAllAppointments(){
		return appointmentService.ShowAllAppointments();
	}
	
	@DeleteMapping(value = "deleteAppointmnet")
	public String deleteAppointments() {
		return appointmentService.ClearAppointments();
	}
	
}
