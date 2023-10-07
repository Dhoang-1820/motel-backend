package com.petproject.motelservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.ERoles;
import com.petproject.motelservice.domain.inventory.Role;

public interface RolesRepository extends JpaRepository<Role, Integer> {
	
	Optional<Role> findByName (ERoles name);
}
