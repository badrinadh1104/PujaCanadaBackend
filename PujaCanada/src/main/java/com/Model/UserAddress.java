package com.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String Address;
	private String city;
	private String province;
	private String country;
	private String zipcode;
	public UserAddress() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserAddress(Long id, String address, String city, String province, String country, String zipcode) {
		super();
		this.id = id;
		Address = address;
		this.city = city;
		this.province = province;
		this.country = country;
		this.zipcode = zipcode;
	}
	
	public UserAddress(String address, String city, String province, String country, String zipcode) {
		super();
		Address = address;
		this.city = city;
		this.province = province;
		this.country = country;
		this.zipcode = zipcode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	
}
