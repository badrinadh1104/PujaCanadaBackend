package com.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.format.annotation.DateTimeFormat;

public class PujaAppointmentDto {
	private Long pujaId;
	private Long userId;
	
	private String appointmentDate;
	
	private String appointmentTime;
	public Long getPujaId() {
		return pujaId;
	}
	public void setPujaId(Long pujaId) {
		this.pujaId = pujaId;
	}
	
	
	
	public String getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
