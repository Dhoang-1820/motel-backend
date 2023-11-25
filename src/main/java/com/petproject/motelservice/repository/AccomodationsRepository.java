package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.Accomodations;

public interface AccomodationsRepository extends JpaRepository<Accomodations, Integer> {
	
	List<Accomodations> findByUserIdAndIsActive(Integer userId, Boolean isActive);
	
}
