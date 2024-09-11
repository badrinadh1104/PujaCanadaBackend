package com.Model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String paymentid;
	private String paymentType;
	@Temporal(TemporalType.TIMESTAMP)
	private float paymentAmount;
	private Date paymentDate;
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name = "user_id")
	private User pujaUser;
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name="order_id")
	private PujaUserOrder userOrder;
	private String paymentStatus;
	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Payment(Long id, String paymentid, String paymentType, float paymentAmount, Date paymentDate, User pujaUser,
			PujaUserOrder userOrder, String paymentStatus) {
		super();
		this.id = id;
		this.paymentid = paymentid;
		this.paymentType = paymentType;
		this.paymentAmount = paymentAmount;
		this.paymentDate = paymentDate;
		this.pujaUser = pujaUser;
		this.userOrder = userOrder;
		this.paymentStatus = paymentStatus;
	}

	public float getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(float paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPaymentid() {
		return paymentid;
	}
	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public User getPujaUser() {
		return pujaUser;
	}
	public void setPujaUser(User pujaUser) {
		this.pujaUser = pujaUser;
	}
	public PujaUserOrder getUserOrder() {
		return userOrder;
	}
	public void setUserOrder(PujaUserOrder userOrder) {
		this.userOrder = userOrder;
	}
	public String isPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String string) {
		this.paymentStatus = string;
	}
	
	
}
