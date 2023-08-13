package com.petproject.motelservice.domain.inventory;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@Getter @Setter
public class Rooms {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "max_capacity")
	private Integer capacity;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "acreage")
	private Double acreage;
	
	@Column(name = "air_conditionor", columnDefinition = "BOOLEAN")
	private Boolean airConditionor;
	
	@Column(name = "internet", columnDefinition = "BOOLEAN")
	private Boolean internet;
	
	@Column(name = "price")
	private Double price;
	
	@Column(name = "is_rent", columnDefinition = "BOOLEAN")
	private Boolean isRent;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accomodation_id")
	private Accomodations accomodations;
	
	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
	private List<Tenants> tenants;
	
	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
	private List<Bills> bills;
	
	@OneToMany(mappedBy = "id.room", fetch = FetchType.LAZY)
	private List<RoomFees> fees;
	
	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
	List<Images> images;

	public Rooms(Integer id) {
		this.id = id;
	}

	public Rooms() {
	}
}
	
