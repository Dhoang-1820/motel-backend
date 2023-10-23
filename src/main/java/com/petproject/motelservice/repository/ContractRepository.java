package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
	
	List<Contract> findByRoomAccomodationsIdAndIsActive(Integer id, Boolean isActive);
	
	
	Contract findByRoomIdAndIsActive(Integer roomId, Boolean isActive);
}
