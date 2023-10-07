package com.petproject.motelservice.domain.inventory;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ContractServiceId implements Serializable{
	
	private static final long serialVersionUID = -950427541601963044L;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
	private Contract contract;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
	private AccomodationUtilities accomodationService;
}
