package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.Tenants;

public interface TenantRepository extends JpaRepository<Tenants, Integer> {
	
	List<Tenants> findByRoomAndIsStayed(Rooms room, Boolean isStayed);
}
