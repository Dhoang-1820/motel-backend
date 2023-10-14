package com.petproject.motelservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.UserDto;
import com.petproject.motelservice.domain.payload.request.ChangePasswordRequest;
import com.petproject.motelservice.domain.payload.request.LoginRequest;
import com.petproject.motelservice.domain.payload.request.SignupRequest;
import com.petproject.motelservice.domain.payload.request.TokenRefreshRequest;
import com.petproject.motelservice.domain.payload.request.UpdateUserRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.domain.payload.response.JwtResponse;
import com.petproject.motelservice.domain.payload.response.TokenRefreshResponse;
import com.petproject.motelservice.domain.query.response.UserResponse;
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
	
	@GetMapping("/info/{userId}")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable Integer userId) {
		final UserDto result = userService.getUserByUserId(userId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping()
	public ResponseEntity<ApiResponse> getAllUser() {
		final List<UserResponse> result = userService.getAllUser();
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PutMapping()
	public ResponseEntity<ApiResponse> saveUser(@RequestBody UpdateUserRequest request) {
		final UpdateUserRequest result = userService.createOrUpdate(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PutMapping("/changePassword")
	public ResponseEntity<ApiResponse> changeUserPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
		 final ApiResponse result = userService.changePassword(changePasswordRequest);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<ApiResponse> addFile(@RequestParam(value = "file", required = false) MultipartFile[] files, @RequestParam("data") String user) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		UserDto dto = mapper.readValue(user, UserDto.class);
		final UserDto result = userService.createOrUpdate(dto, files);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.CREATE_SUCCESS_MSG));
	}

}
