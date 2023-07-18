package com.petproject.motelservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petproject.motelservice.domain.payload.request.LoginRequest;
import com.petproject.motelservice.domain.payload.request.SignupRequest;
import com.petproject.motelservice.domain.payload.request.TokenRefreshRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.domain.payload.response.JwtResponse;
import com.petproject.motelservice.domain.payload.response.TokenRefreshResponse;
import com.petproject.motelservice.services.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/auth/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
		final JwtResponse result = userService.signIn(loginRequest);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PostMapping("/auth/refreshtoken")
	public ResponseEntity<TokenRefreshResponse> refreshtoken(@RequestBody TokenRefreshRequest request) {
		final TokenRefreshResponse result = userService.getRefreshtoken(request);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PostMapping("/auth/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		final ApiResponse result = userService.signUp(signUpRequest);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}
