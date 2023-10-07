package com.petproject.motelservice.domain.inventory;

import java.util.List;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "accomodation_services")
@Getter @Setter
public class AccomodationUtilities extends BaseEntity {
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "price")
	private Double price;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "description")
	private String description;
	
	@OneToMany(mappedBy = "id.accomodationService", fetch = FetchType.LAZY)
	private List<ContractService> contractServices;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accomodation_id", nullable = false)
	private Accomodations accomodation;
}
