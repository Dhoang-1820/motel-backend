package com.petproject.motelservice.security.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.inventory.RefreshToken;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.exeption.TokenRefreshException;
import com.petproject.motelservice.repository.RefreshTokenRepository;
import com.petproject.motelservice.repository.UsersRepository;
import com.petproject.motelservice.security.jwt.JwtUtils;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {

	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	@Autowired
	UsersRepository userRepository;
	
	@Autowired
	JwtUtils jwtUtils;
	
	public Optional<RefreshToken> getByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}
	
	public RefreshToken getByUserId(Integer userId) {
		return refreshTokenRepository.findByUserId(userId);
	}

	public RefreshToken createRefreshToken(Integer userId) {
		RefreshToken refreshToken = new RefreshToken();
		Users user = userRepository.findById(userId).get();
		refreshToken.setUser(user);
		refreshToken.setToken(jwtUtils.doGenerateRefreshToken(user.getUsername()));
		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (!jwtUtils.validateJwtToken(token.getToken())) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
		}
		return token;
	}

	@Transactional
	public int deleteByUserId(Integer userId) {
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}
}
