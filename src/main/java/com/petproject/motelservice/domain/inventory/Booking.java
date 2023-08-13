package com.petproject.motelservice.domain.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "booking")
@Getter @Setter
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "customer_name")
	private String name;
	
	@Column(name = "customer_phone")
	private String phone;
	
	@Column(name = "customer_email")
	private String email;
	
	@Column(name = "room_id")
	private String roomId;
}
