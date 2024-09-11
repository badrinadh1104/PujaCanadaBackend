package com.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Puja {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Lob
	private String description;
	private float price;
	private int discount;
	@JsonIgnore
	private byte[] image;
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "priestPujas")
	private Set<Priest> priests;

	public Puja() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Puja(Long id, String name, String description, float price, int discount, byte[] image,
			Set<Priest> priests) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.discount = discount;
		this.image = image;

		this.priests = priests;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

//	public ResponseEntity<byte[]> getImage() {
//		if(this.image != null) {
//			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
//		}
//		return ResponseEntity.notFound().build();
//	}
	
	

	public void setImage(byte[] image) {
		this.image = image;
	}

	public byte[] getImage() {
		return image;
	}

	public Set<Priest> getPriests() {
		return priests;
	}

	public void setPriests(Set<Priest> priests) {
		this.priests = priests;
	}

//	public List<PujaAppointment> getPujaAppointments() {
//		return pujaAppointments;
//	}
//
//	public void setPujaAppointments(List<PujaAppointment> pujaAppointments) {
//		this.pujaAppointments = pujaAppointments;
//	}
//
//	public void AddAppointment(PujaAppointment appointment) {
//		if (this.pujaAppointments == null) {
//			this.pujaAppointments = new ArrayList<PujaAppointment>();
//		}
//		this.pujaAppointments.add(appointment);
//	}

}
