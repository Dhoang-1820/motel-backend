package com.petproject.motelservice.services;

import com.petproject.motelservice.domain.payload.request.LoginRequest;
import com.petproject.motelservice.domain.payload.request.SignupRequest;
import com.petproject.motelservice.domain.payload.request.TokenRefreshRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.domain.payload.response.JwtResponse;
import com.petproject.motelservice.domain.payload.response.TokenRefreshResponse;

public interface UserService {
	
	public JwtResponse signIn(LoginRequest loginRequest);
	
	public ApiResponse signUp(SignupRequest signUpRequest);
	
	public TokenRefreshResponse getRefreshtoken(TokenRefreshRequest request);
}
