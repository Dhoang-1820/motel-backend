package com.petproject.motelservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.ERoles;
import com.petproject.motelservice.domain.inventory.Roles;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
	
	Optional<Roles> findByName (ERoles name);
}
