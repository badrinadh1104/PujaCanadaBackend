package com.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Earnings {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name = "priest_id")
	private Priest priest;
	@OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name = "appointment_id")
	private PujaAppointment pujaAppointment;
	private float Amount;
	public Earnings() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Earnings(Long id, Priest priest, PujaAppointment pujaAppointment, float amount) {
		super();
		this.id = id;
		this.priest = priest;
		this.pujaAppointment = pujaAppointment;
		Amount = amount;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Priest getPriest() {
		return priest;
	}
	public void setPriest(Priest priest) {
		this.priest = priest;
	}
	public PujaAppointment getPujaAppointment() {
		return pujaAppointment;
	}
	public void setPujaAppointment(PujaAppointment pujaAppointment) {
		this.pujaAppointment = pujaAppointment;
	}
	public float getAmount() {
		return Amount;
	}
	public void setAmount(float amount) {
		Amount = amount;
	}
	
	

}
