package com.Model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String userName;
	private String email;
	private String phone;
	private String password;
	private String role;
	
//	user has userAddress (One to One relation)
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userAddress_id")
	private UserAddress userAddress;
	
//	User has a wishlist (One to One Bi-directional )
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "wishlist_id")
	private WishList userWishList;
	
//	User has a Cartlist (One to One Bi-directional)
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cartlist_id")
	private CartList userCartList;
	
//	User has many orders (one to many Relationship)
//	but upon deleting the user , i don't wan'na delete the orders list 
	@JsonIgnore
	@OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},mappedBy = "user")
	private List<PujaUserOrder> orders;

	@JsonIgnore
	@OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},mappedBy = "userPujaAppointment")
	private List<PujaAppointment> pujaAppointments;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<PujaUserOrder> userOrderHistory;
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

	public User(Long id, String userName, String email, String phone, String password, String role,
			UserAddress userAddress, WishList userWishList, CartList userCartList, List<PujaUserOrder> orders,
			List<PujaAppointment> pujaAppointments, List<PujaUserOrder> userOrderHistory) {
		super();
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.role = role;
		this.userAddress = userAddress;
		this.userWishList = userWishList;
		this.userCartList = userCartList;
		this.orders = orders;
		this.pujaAppointments = pujaAppointments;
		this.userOrderHistory = userOrderHistory;
	}




	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public UserAddress getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(UserAddress userAddress) {
		this.userAddress = userAddress;
	}

	public WishList getUserWishList() {
		return userWishList;
	}

	public void setUserWishList(WishList userWishList) {
		this.userWishList = userWishList;
	}

	public CartList getUserCartList() {
		return userCartList;
	}

	public void setUserCartList(CartList userCartList) {
		this.userCartList = userCartList;
	}

	public List<PujaUserOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<PujaUserOrder> orders) {
		this.orders = orders;
	}
	
	

	public List<PujaUserOrder> getUserOrderHistory() {
		return userOrderHistory;
	}




	public void setUserOrderHistory(List<PujaUserOrder> userOrderHistory) {
		this.userOrderHistory = userOrderHistory;
	}




	
	public void AddOrder(PujaUserOrder order) {
		if(orders == null) {
			orders = new ArrayList<PujaUserOrder>();
		}
		orders.add(order);
	}

	public List<PujaAppointment> getPujaAppointments() {
		return pujaAppointments;
	}

	public void setPujaAppointments(List<PujaAppointment> pujaAppointments) {
		this.pujaAppointments = pujaAppointments;
	}
	
	
	public void AddAppointment(PujaAppointment appointment) {
		if(this.pujaAppointments == null) {
			this.pujaAppointments = new ArrayList<PujaAppointment>();
		}
		this.pujaAppointments.add(appointment);
	}
	
	
	public void AddOrderToHistory(PujaUserOrder pujaUserOrder) {
		if(this.userOrderHistory == null ) {
			this.userOrderHistory = new ArrayList<PujaUserOrder>();
		}
		this.userOrderHistory.add(pujaUserOrder);
	}
	
}
