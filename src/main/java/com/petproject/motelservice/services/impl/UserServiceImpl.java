package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.inventory.ERoles;
import com.petproject.motelservice.domain.inventory.RefreshToken;
import com.petproject.motelservice.domain.inventory.Roles;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.payload.request.LoginRequest;
import com.petproject.motelservice.domain.payload.request.SignupRequest;
import com.petproject.motelservice.domain.payload.request.TokenRefreshRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.domain.payload.response.JwtResponse;
import com.petproject.motelservice.domain.payload.response.TokenRefreshResponse;
import com.petproject.motelservice.repository.RolesRepository;
import com.petproject.motelservice.repository.UsersRepository;
import com.petproject.motelservice.security.jwt.JwtUtils;
import com.petproject.motelservice.security.services.RefreshTokenService;
import com.petproject.motelservice.security.services.UserDetailsImpl;
import com.petproject.motelservice.services.UserService;

@Service
public class UserServiceImpl implements UserService {

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

	@Override
	public JwtResponse signIn(LoginRequest loginRequest) {
		JwtResponse jwtResponse = new JwtResponse();
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());
		
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
			String token = jwtUtils.generateJwtToken(user.getUsername());
			refreshResponse.setAccessToken(token);
			refreshResponse.setRefreshToken(refreshToken.getToken());
		}
		return refreshResponse;
	}

	@Override
	public ApiResponse signUp(SignupRequest signUpRequest) {
		Users user = new Users();
		user.setEmail(signUpRequest.getEmail());
		user.setAddress(signUpRequest.getAddress());
		user.setUsername(signUpRequest.getUserName());
		user.setPhone(signUpRequest.getPhone());
		user.setActive(Boolean.TRUE);
		user.setPassword(encoder.encode(signUpRequest.getPassword()));

		List<String> strRoles = signUpRequest.getRoles();
		List<Roles> roles = new ArrayList<>();

		strRoles.forEach(role -> {
			switch (role) {
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
		});

		user.setRole(roles);
		user.setCreatedAt(new Date());
		usersRepository.save(user);
		return null;
	}

}
