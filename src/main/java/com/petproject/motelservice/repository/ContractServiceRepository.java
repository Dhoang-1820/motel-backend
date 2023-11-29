package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.petproject.motelservice.domain.inventory.ContractService;
import com.petproject.motelservice.domain.inventory.ContractServiceId;

public interface ContractServiceRepository extends JpaRepository<ContractService, ContractServiceId> {
	
	
	@Modifying
	@Transactional
	int deleteByIdContractId(Integer contractId);
}
