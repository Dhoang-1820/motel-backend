package com.petproject.motelservice.domain.inventory;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "equipments")
public class Equipments extends BaseEntity {
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "price")
	private Double price;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", nullable = true)
	private Rooms room;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accomodation_id", nullable = false)
	private Accomodations accomodations;
	
}
