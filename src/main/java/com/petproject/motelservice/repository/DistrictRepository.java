package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.District;

public interface DistrictRepository extends JpaRepository<District, Integer> {

	District findByDistrictCode(Integer districtCode);
}
