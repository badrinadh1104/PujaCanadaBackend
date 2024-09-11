package com.Model;

import java.util.ArrayList;
import java.util.List;

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
public class PujaUserOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH} )
	@JoinColumn(name = "userid")
	private User user;
	
//	pending mapping 
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "order_products",joinColumns = @JoinColumn(name="order_id"),inverseJoinColumns = @JoinColumn(name = "product_id"))
//	private List<CartItem> orderedProducts;
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "order_pujas",joinColumns = @JoinColumn(name="order_id"),inverseJoinColumns = @JoinColumn(name = "puja_id"))
//	private List<Puja> orderedPujas;
	
	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "orderId")
	private List<CartItem> orderedProducts;
	
	@OneToMany( cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH })
	@JoinColumn(name = "orderId")
	private List<PujaAppointment> pujaOrders;
	
//	Pending payment implementation
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "userOrder")
	private List<Payment> payments;
	
//	many orders can be assigned to one delivery agent
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private DeliveryAgent deliveryAgent;
	private float total;
	private boolean paymentStatus;
	private boolean deliveryStatus;
	public PujaUserOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PujaUserOrder(Long id, User user, List<CartItem> orderedProducts, List<PujaAppointment> pujaAppointments, List<Payment> payment,
			DeliveryAgent deliveryAgent, float total, boolean paymentStatus,boolean deliveryStatus) {
		super();
		this.id = id;
		this.user = user;
		this.orderedProducts = orderedProducts;
		this.pujaOrders = pujaAppointments;
		this.payments = payment;
		this.deliveryAgent = deliveryAgent;
		this.total = total;
		this.paymentStatus = paymentStatus;
		this.deliveryStatus=deliveryStatus;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<CartItem> getOrderedProducts() {
		return orderedProducts;
	}
	public void setOrderedProducts(List<CartItem> orderedProducts) {
		this.orderedProducts = orderedProducts;
	}
	public List<PujaAppointment> getPujaOrders() {
		return pujaOrders;
	}
	public void setPujaOrders(List<PujaAppointment> pujaOrders) {
		this.pujaOrders = pujaOrders;
	}
	public List<Payment> getPayments() {
		return payments;
	}
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	public DeliveryAgent getDeliveryAgent() {
		return deliveryAgent;
	}
	public void setDeliveryAgent(DeliveryAgent deliveryAgent) {
		this.deliveryAgent = deliveryAgent;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public boolean isPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public boolean isDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(boolean deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public void AddtouserOrderedProducts(CartItem product) {
		if(orderedProducts == null) {
			orderedProducts = new ArrayList<CartItem>();
		}
		orderedProducts.add(product);
		
	}
	
//	public void AddtouserOrderedPujas(Puja puja) {
//		if(orderedPujas == null) {
//			orderedPujas = new ArrayList<Puja>();
//		}
//		orderedPujas.add(puja);
//	}
	
	public void AddPujatoOrder(PujaAppointment puja) {
		if (pujaOrders == null) {
			pujaOrders = new ArrayList<PujaAppointment>();
		}
		pujaOrders.add(puja);
	}
	
	public void Addpayment(Payment payment) {
		if(payments == null) {
			payments = new ArrayList<Payment>();
		}
		payments.add(payment);
	}
	
	
	
	
}
