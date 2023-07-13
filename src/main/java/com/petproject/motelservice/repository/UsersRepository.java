package com.petproject.motelservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petproject.motelservice.domain.inventory.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {
	
	Optional<Users> findByUsername(String usename);
	
	Boolean existsByUsername (String username);
	
	Boolean existsByEmail (String email);
}
