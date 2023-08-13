package com.petproject.motelservice.domain.inventory;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bills")
@Getter @Setter
public class Bills {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "date")
	private Date billDate;
	
	@Column(name = "first_electric_num")
	private Integer firstElectric;
	
	@Column(name = "last_electric_num")
	private Integer lastElectric;
	
	@Column(name = "electric_num")
	private Integer electricNum;
	
	@Column(name = "first_water_num")
	private Integer firstWater;
	
	@Column(name = "last_water_num")
	private Integer lastWater;
	
	@Column(name = "water_num")
	private Integer waterNum;
	
	@Column(name = "total_price")
	private Double totalPrice;
	
	@Column(name = "is_pay", columnDefinition = "BOOLEAN")
	private Boolean isPay;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "is_sent", columnDefinition = "BOOLEAN")
	private Boolean isSent;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Rooms room;
}
