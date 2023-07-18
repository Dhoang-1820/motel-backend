package com.petproject.motelservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.petproject.motelservice.domain.inventory.RefreshToken;
import com.petproject.motelservice.domain.inventory.Users;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

	Optional<RefreshToken> findByToken(String token);
	
	RefreshToken findByUserId(Integer userId);

	@Modifying
	int deleteByUser(Users user);
}
