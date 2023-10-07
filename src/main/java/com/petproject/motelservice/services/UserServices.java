package com.petproject.motelservice.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.domain.dto.UserDto;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.payload.request.ChangePasswordRequest;
import com.petproject.motelservice.domain.payload.request.LoginRequest;
import com.petproject.motelservice.domain.payload.request.SignupRequest;
import com.petproject.motelservice.domain.payload.request.TokenRefreshRequest;
import com.petproject.motelservice.domain.payload.request.UpdateUserRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.domain.payload.response.JwtResponse;
import com.petproject.motelservice.domain.payload.response.TokenRefreshResponse;
import com.petproject.motelservice.domain.query.response.UserResponse;

public interface UserServices {
	
	public JwtResponse signIn(LoginRequest loginRequest);
	
	public ApiResponse signUp(SignupRequest signUpRequest);
	
	public TokenRefreshResponse getRefreshtoken(TokenRefreshRequest request);
	
	public Users getUserById(Integer userId);
	
	public UserDto getUserByUserId(Integer userId);
	
	public UserDto createOrUpdate(UserDto user, MultipartFile[] image);
	
	public ApiResponse changePassword(ChangePasswordRequest changePasswordRequest);
	
	public List<UserResponse> getAllUser();
	
	public UpdateUserRequest createOrUpdate(UpdateUserRequest request);
}
