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
import jakarta.persistence.OneToOne;

@Entity
public class WishList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},mappedBy = "userWishList")
	private User pujaUser;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "wishlist_products",joinColumns = @JoinColumn(name = "wishlist_id"),inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<Product> userWishlistProducts;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "wishlist_puja",joinColumns = @JoinColumn(name = "wishlist_id"),inverseJoinColumns = @JoinColumn(name = "puja_id"))
	private List<Puja> userPujas;
	public WishList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WishList(Long id, User pujaUser, List<Product> userWishlistProducts, List<Puja> userPujas) {
		super();
		this.id = id;
		this.pujaUser = pujaUser;
		this.userWishlistProducts = userWishlistProducts;
		this.userPujas = userPujas;
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
	public List<Product> getUserWishlistProducts() {
		return userWishlistProducts;
	}
	public void setUserWishlistProducts(List<Product> userWishlistProducts) {
		this.userWishlistProducts = userWishlistProducts;
	}
	public List<Puja> getUserPujas() {
		return userPujas;
	}
	public void setUserPujas(List<Puja> userPujas) {
		this.userPujas = userPujas;
	}
	
	
	public void AddProducttowishlist(Product product) {
		if(userWishlistProducts == null) {
			userWishlistProducts = new ArrayList<Product>();
		}
		userWishlistProducts.add(product);
		
	}
	
	public void AddPujatoWishlistPuja(Puja puja) {
		if(userPujas == null) {
			userPujas = new ArrayList<Puja>();
		}
		userPujas.add(puja);
	}
	
	
	

}
