package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.FileUploadDto;
import com.petproject.motelservice.domain.dto.UserDto;
import com.petproject.motelservice.domain.inventory.ERoles;
import com.petproject.motelservice.domain.inventory.RefreshToken;
import com.petproject.motelservice.domain.inventory.Roles;
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
import com.petproject.motelservice.repository.RolesRepository;
import com.petproject.motelservice.repository.UsersRepository;
import com.petproject.motelservice.security.jwt.JwtUtils;
import com.petproject.motelservice.security.services.RefreshTokenService;
import com.petproject.motelservice.security.services.UserDetailsImpl;
import com.petproject.motelservice.services.FileService;
import com.petproject.motelservice.services.UserServices;

@Service
public class UserServicesImpl implements UserServices {

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	RolesRepository rolesRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RefreshTokenService refreshTokenService;
	
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	FileService storageService;

	@Override
	public JwtResponse signIn(LoginRequest loginRequest) {
		JwtResponse jwtResponse = new JwtResponse();
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		RefreshToken refreshToken = refreshTokenService.getByUserId(userDetails.getId());
		if (refreshToken != null ) {
			refreshTokenService.deleteByUserId(userDetails.getId());
		}
		refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		
		jwtResponse.setId(userDetails.getId());
		jwtResponse.setEmail(userDetails.getEmail());
		jwtResponse.setUsername(userDetails.getUsername());
		jwtResponse.setRefreshToken(refreshToken.getToken());
		jwtResponse.setToken(jwt);
		jwtResponse.setRoles(roles);
		return jwtResponse;
	}

	@Override
	public TokenRefreshResponse getRefreshtoken(TokenRefreshRequest request) {
		TokenRefreshResponse refreshResponse = new TokenRefreshResponse();
		String requestRefreshToken = request.getRefreshToken();
		RefreshToken refreshToken = refreshTokenService.getByToken(requestRefreshToken).orElse(null);
		if (refreshToken != null) {
			refreshToken = refreshTokenService.verifyExpiration(refreshToken);
			Users user = refreshToken.getUser();
			String token = jwtUtils.generateTokenFromUsername(user.getUsername());
			refreshResponse.setAccessToken(token);
			refreshResponse.setRefreshToken(refreshToken.getToken());
		}
		return refreshResponse;
	}

	@Override
	public ApiResponse signUp(SignupRequest signUpRequest) {
		Users user = new Users();
		ApiResponse response = new ApiResponse();
		user.setEmail(signUpRequest.getEmail());
		user.setAddress(signUpRequest.getAddress());
		user.setUsername(signUpRequest.getUserName());
		user.setPhone(signUpRequest.getPhone());
		user.setFirstname(signUpRequest.getFirstName());
		user.setLastname(signUpRequest.getLastName());
		user.setActive(Boolean.TRUE);
		user.setPassword(encoder.encode(signUpRequest.getPassword()));

		String strRoles = signUpRequest.getRoles();
		List<Roles> roles = new ArrayList<>();
		
		switch (strRoles) {
			case "admin":
				Roles adminRole = rolesRepository.findByName(ERoles.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(adminRole);
				break;
			case "mod":
				Roles modRole = rolesRepository.findByName(ERoles.ROLE_MODERATOR)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(modRole);
				break;
			case "tenant":
				Roles tenantRole = rolesRepository.findByName(ERoles.ROLE_TENANT)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(tenantRole);
				break;
			case "landlord":
				Roles landlordRole = rolesRepository.findByName(ERoles.ROLE_LANDLORD)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(landlordRole);
				break;
			default:
				Roles userRole = rolesRepository.findByName(ERoles.ROLE_LANDLORD)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
		}

		user.setRole(roles);
		user.setCreatedAt(new Date());
		usersRepository.save(user);
		response.setData(user);
		response.setMessage(Constants.SIGNUP_SUCCESS_MSG);
		response.setSuccess(Boolean.TRUE);
		return response;
	}

	@Override
	public Users getUserById(Integer userId) {
		return usersRepository.findById(userId).orElse(null);
	}

	@Override
	public UserDto getUserByUserId(Integer userId) {
		UserDto result = new UserDto();
		Users user = usersRepository.findByUserId(userId);
		result = mapper.map(user, UserDto.class);
		return result;
	}

	@Override
	public UserDto createOrUpdate(UserDto request, MultipartFile[] image) {
		Users user = null;
		if (request.getId() == null) {
			user = new Users();
		} else {
			user = usersRepository.findById(request.getId()).orElse(null);
		}
		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.setEmail(request.getEmail());
		user.setAddress(request.getAddress());
		user.setPhone(request.getPhone());
		
		if (image != null) {
			List<FileUploadDto> imgResult = storageService.uploadFiles(image);
			user.setImageUrl(imgResult.get(0).getFileUrl());			
		}
		usersRepository.save(user);
		UserDto result = mapper.map(user, UserDto.class);
		return result;
	}

	@Override
	public ApiResponse changePassword(ChangePasswordRequest changePasswordRequest) {
		ApiResponse response = new ApiResponse();
		String oldPassword = changePasswordRequest.getOldPassword();
		Users user = usersRepository.findById(changePasswordRequest.getUserId()).orElse(null);
		if (encoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
			response.setSuccess(true);
			user = usersRepository.save(user);
		} else {
			response.setSuccess(false);
			response.setMessage("Mật khẩu cũ không chính xác");
		}
		
		return response;
	}

	@Override
	public List<UserResponse> getAllUser() {
		return usersRepository.findAllUsers();
	}

	@Override
	public UpdateUserRequest createOrUpdate(UpdateUserRequest request) {
		Users user = null;
		UpdateUserRequest result = null;
		if (request.getUserId() != null ) {
			user = usersRepository.findByUserId(request.getUserId());
			user.setFirstname(request.getFirstName());
			user.setLastname(request.getLastName());
			user.setActive(request.getActive());
			user.setEmail(request.getEmail());
			user.setAddress(request.getAddress());
			user.setPhone(request.getPhone());
			
			user = usersRepository.save(user);
			result = mapper.map(user, UpdateUserRequest.class);
		} else {
			SignupRequest sigupRequest = new SignupRequest();
			sigupRequest.setAddress(request.getEmail());
			sigupRequest.setEmail(request.getEmail());
			sigupRequest.setPassword(request.getPassword());
			sigupRequest.setPhone(request.getPhone());
			sigupRequest.setUserName(request.getUserName());
			sigupRequest.setFirstName(request.getFirstName());
			sigupRequest.setLastName(request.getLastName());
			sigupRequest.setRoles(request.getRole());
			signUp(sigupRequest);
		}
		
		return result;
	}
	
	
}
