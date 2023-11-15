package com.petproject.motelservice.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.domain.dto.BankAccountDto;
import com.petproject.motelservice.domain.dto.DashBoardDto;
import com.petproject.motelservice.domain.dto.UserDto;
import com.petproject.motelservice.domain.dto.UserPreferenceDto;
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

public interface UserService {
	
	JwtResponse signIn(LoginRequest loginRequest);
	
	ApiResponse signUp(SignupRequest signUpRequest);
	
	TokenRefreshResponse getRefreshtoken(TokenRefreshRequest request);
	
	Users getUserById(Integer userId);
	
	UserDto getUserByUserId(Integer userId);
	
	BankAccountDto saveBankAccount(BankAccountDto request);
	
	void removeBankAccount(Integer bankId);
	
	UserDto createOrUpdate(UserDto user, MultipartFile[] image);
	
	ApiResponse changePassword(ChangePasswordRequest changePasswordRequest);
	
	List<UserResponse> getAllUser();
	
	UpdateUserRequest createOrUpdate(UpdateUserRequest request);
	
	UserPreferenceDto getUserConfigByUserId(Integer userId);

	UserPreferenceDto updateUserPreference(UserPreferenceDto request);
	
	DashBoardDto getUserDashboard(Integer userId);
}
