package com.Model;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

@Entity
//@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "puja_id" }) })
public class PujaAppointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "puja_id")
	private Puja puja;
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	@Temporal(TemporalType.DATE)
	@Schema(type = "string", format = "date", example = "2024-09-01")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate appointmentDate;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime appointmentTime;
//	private Time appointmentTime;
	private boolean isConfirmed;
//	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "user_id")
	private User userPujaAppointment;
	private boolean completedStatus;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "Priest_id")
	private Priest pujaPriest;

	private float pujaFee;

	public PujaAppointment() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public PujaAppointment(Long id, Puja puja, LocalDate appointmentDate, LocalTime appointmentTime,
			boolean isConfirmed, User userPujaAppointment, boolean completedStatus, Priest pujaPriest, float pujaFee) {
		super();
		this.id = id;
		this.puja = puja;
		this.appointmentDate = appointmentDate;
		this.appointmentTime = appointmentTime;
		this.isConfirmed = isConfirmed;
		this.userPujaAppointment = userPujaAppointment;
		this.completedStatus = completedStatus;
		this.pujaPriest = pujaPriest;
		this.pujaFee = pujaFee;
	}



	public float getPujaFee() {
		return pujaFee;
	}

	public void setPujaFee(float pujaFee) {
		this.pujaFee = pujaFee;
	}

	public Priest getPujaPriest() {
		return pujaPriest;
	}

	public void setPujaPriest(Priest pujaPriest) {
		this.pujaPriest = pujaPriest;
	}

	public boolean isCompletedStatus() {
		return completedStatus;
	}

	public void setCompletedStatus(boolean completedStatus) {
		this.completedStatus = completedStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Puja getPuja() {
		return puja;
	}

	public void setPuja(Puja puja) {
		this.puja = puja;
	}

	

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}



	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}



	public String getAppointmentTime() {
//		return appointmentTime;
		return appointmentTime.format(DateTimeFormatter.ofPattern("h:mm a"));
	}



	public void setAppointmentTime(LocalTime appointmentTime) {
		this.appointmentTime = appointmentTime;
	}



	public boolean isConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public User getUserPujaAppointment() {
		return userPujaAppointment;
	}

	public void setUserPujaAppointment(User user) {
		this.userPujaAppointment = user;
	}

}
