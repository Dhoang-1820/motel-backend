package com.petproject.motelservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petproject.motelservice.domain.inventory.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
	
	List<Contract> findByRoomAccomodationsIdAndIsActive(Integer id, Boolean isActive);
	
	@Query("SELECT contract FROM Contract contract INNER JOIN ContractService contractService ON contract = contractService.id.contract INNER JOIN AccomodationUtilities accomodationService ON contractService.id.accomodationService = accomodationService WHERE accomodationService.accomodation.id = :accomodationId AND accomodationService.id = :serviceId AND contract.endDate > :now")
	List<Contract> findByAccomodationAndService(Integer accomodationId, Integer serviceId, Date now);
	
	Contract findByRoomIdAndIsActive(Integer roomId, Boolean isActive);
}
