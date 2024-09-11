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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class CartList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, mappedBy = "userCartList")
	private User pujaUser;
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "cartlist_products",joinColumns = @JoinColumn(name = "cart_id"),inverseJoinColumns = @JoinColumn(name = "product_id"))
//	private List<Product> userCartlistProducts;
	
	@OneToMany(mappedBy = "cartList", cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	private List<CartItem> cartItems;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "cartlist_pujaAppointments", joinColumns = @JoinColumn(name = "cart_id"), inverseJoinColumns = @JoinColumn(name = "PujaAppointment_id"))
	private List<PujaAppointment> userCartListPujas;
	
	private float cartTotal;
	
//	@OneToMany()
//	private List<PujaAppointment> userCartListPujas;

	public CartList() {
		super();
		// TODO Auto-generated constructor stub
	}

	





	public CartList(Long id, User pujaUser, List<CartItem> cartItems, List<PujaAppointment> userCartListPujas,float cartTotal) {
		super();
		this.id = id;
		this.pujaUser = pujaUser;
		this.cartItems = cartItems;
		this.userCartListPujas = userCartListPujas;
		this.cartTotal = cartTotal;
	}







	public float getCartTotal() {
		return cartTotal;
	}







	public void setCartTotal(float cartTotal) {
		this.cartTotal = cartTotal;
	}







	public List<CartItem> getCartItems() {
		return cartItems;
	}



	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getPujaUser() {
		return pujaUser;
	}

	public void setPujaUser(User pujaUser) {
		this.pujaUser = pujaUser;
	}

//	public List<Product> getUserCartlistProducts() {
//		return userCartlistProducts;
//	}
//
//	public void setUserCartlistProducts(List<Product> userCartlistProducts) {
//		this.userCartlistProducts = userCartlistProducts;
//	}

	

//	public void AddProducttoCartlist(Product product) {
//		if (userCartlistProducts == null) {
//			userCartlistProducts = new ArrayList<Product>();
//		}
//		userCartlistProducts.add(product);
//
//	}

	public void AddPujatoCartlistPuja(PujaAppointment puja) {
		if (userCartListPujas == null) {
			userCartListPujas = new ArrayList<PujaAppointment>();
		}
		userCartListPujas.add(puja);
	}
	
	public List<PujaAppointment> getUserCartListPujas() {
		return userCartListPujas;
	}







	public void setUserCartListPujas(List<PujaAppointment> userCartListPujas) {
		this.userCartListPujas = userCartListPujas;
	}







	public void AddCartItem(CartItem cartItem) {
		if(this.cartItems == null) {
			this.cartItems = new ArrayList<CartItem>();
		}
		this.cartItems.add(cartItem);
	}

}
