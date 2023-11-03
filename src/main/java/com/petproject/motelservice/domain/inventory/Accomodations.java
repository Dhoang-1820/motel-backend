package com.petproject.motelservice.domain.inventory;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.CascadeType;
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
@Table(name = "accomodations")
@Getter
@Setter
public class Accomodations extends BaseEntity {

	@Column(name = "name")
	private String name;

	@Column(name = "created_at")
	private Date createAt;

	@Column(name = "active", columnDefinition = "BOOLEAN")
	private Boolean isActive = Boolean.TRUE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users user;

	@OneToMany(mappedBy = "accomodations", fetch = FetchType.LAZY)
	private List<Rooms> rooms;

	@OneToMany(mappedBy = "accomodations", fetch = FetchType.LAZY)
	private List<Equipments> equipments;

	@OneToMany(mappedBy = "accomodations", fetch = FetchType.LAZY)
	private List<Tenants> tenants;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;

}
