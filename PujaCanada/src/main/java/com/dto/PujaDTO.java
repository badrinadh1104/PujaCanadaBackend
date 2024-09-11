package com.dto;

public class PujaDTO {
	private String name;
	private String description;
	private float price;
	private int discount;

	public PujaDTO(String name, String description, float price, int discount) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.discount = discount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

 	}
