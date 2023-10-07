package com.petproject.motelservice.domain.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "contract_service")
public class ContractService {
	
	@EmbeddedId
	private ContractServiceId id;
	
	@Column(name = "quantity")
	private Integer quantity;
}
