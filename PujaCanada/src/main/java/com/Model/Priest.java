package com.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Priest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PriestUserId")
	private User user;
//	one user can perform many puja's 
//	this is used to populate all the priests of particular puja.
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "priest_pujas",joinColumns = @JoinColumn(name="priest_id"),inverseJoinColumns = @JoinColumn(name = "puja_id"))
	private Set<Puja> priestPujas;
	
//	 Bookings appointed to particular priest.
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name = "pujaTodoID")
	private List<PujaAppointment> pujasTodo;
	
//	@JoinTable(name = "priestbooking_pujas",joinColumns = @JoinColumn(name="priest_id"),inverseJoinColumns = @JoinColumn(name = "puja_id"))
	
//	this is used to know the earnings of priest
//	one to many bidirectional
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},mappedBy = "priest")
	private List<Earnings> priestEarnings;
	
	private boolean isAvailable;

	public Priest() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public Priest(Long id, User user, Set<Puja> priestPujas, List<PujaAppointment> pujasTodo,
			List<Earnings> priestEarnings, boolean isAvailable) {
		super();
		this.id = id;
		this.user = user;
		this.priestPujas = priestPujas;
		this.pujasTodo = pujasTodo;
		this.priestEarnings = priestEarnings;
		this.isAvailable = isAvailable;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	public boolean isAvailable() {
		return isAvailable;
	}


	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Puja> getPriestPujas() {
		return priestPujas;
	}

	public void setPriestPujas(Set<Puja> priestPujas) {
		this.priestPujas = priestPujas;
	}

	public List<PujaAppointment> getPujasTodo() {
		return pujasTodo;
	}

	public void setPujasTodo(List<PujaAppointment> pujasTodo) {
		this.pujasTodo = pujasTodo;
	}

	public List<Earnings> getPriestEarnings() {
		return priestEarnings;
	}

	public void setPriestEarnings(List<Earnings> priestEarnings) {
		this.priestEarnings = priestEarnings;
	}
	

	public void AddpujastoPriest(Puja puja) {
		if(this.priestPujas == null) {
			this.priestPujas = new HashSet<Puja>();
		}
		priestPujas.add(puja);
	}
	
	public void PriestpujaTodo(PujaAppointment pujaAppointment) {
		if(this.pujasTodo == null) {
			this.pujasTodo = new ArrayList<PujaAppointment>();
		}
		this.pujasTodo.add(pujaAppointment);
	}
}
