package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petproject.motelservice.domain.inventory.Contract;
import com.petproject.motelservice.domain.inventory.ContractServiceId;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
	
	List<Contract> findByRoomAccomodationsIdAndIsActive(Integer id, Boolean isActive);
	
}
