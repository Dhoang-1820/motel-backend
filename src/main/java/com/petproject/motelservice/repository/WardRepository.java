package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.Ward;

public interface WardRepository extends JpaRepository<Ward, Integer> {

	Ward findByWardCode(Integer wardCode);
}
