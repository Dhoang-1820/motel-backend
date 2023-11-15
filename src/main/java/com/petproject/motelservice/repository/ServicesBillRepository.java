package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.ServicesBill;

public interface ServicesBillRepository extends JpaRepository<ServicesBill, Integer> {

	
	List<ServicesBill> findByBillId(Integer id);
}
