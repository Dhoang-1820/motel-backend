package com.petproject.motelservice.domain.inventory;

import java.util.List;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@Getter @Setter
public class Rooms extends BaseEntity{
	
	@Column(name = "max_capacity")
	private Integer capacity;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "acreage")
	private Double acreage;
	
	@Column(name = "price")
	private Double price;
	
	@Column(name = "is_rent", columnDefinition = "BOOLEAN")
	private Boolean isRent;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accomodation_id")
	private Accomodations accomodations;
	
	@OneToOne(mappedBy = "room")
	private Post post;
	
	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
	private List<Deposits> deposits;

	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
	private List<Bills> invoices;
	
	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
	private List<Equipments> equipments;
	
	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
	private List<ElectricWaterNum> electricWaterNums;
	
	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
	private List<Contract> contracts;

	public Rooms(Integer id) {
		super.setId(id);
	}

	public Rooms() {
	}
}
	
