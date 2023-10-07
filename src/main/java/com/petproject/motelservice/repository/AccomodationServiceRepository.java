package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.AccomodationUtilities;

public interface AccomodationServiceRepository extends JpaRepository<AccomodationUtilities, Integer> {
	
	List<AccomodationUtilities> findByAccomodationId(Integer accomodationId);
}
