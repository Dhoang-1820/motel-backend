package com.petproject.motelservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petproject.motelservice.domain.inventory.ERoles;
import com.petproject.motelservice.domain.inventory.Roles;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.payload.request.LoginRequest;
import com.petproject.motelservice.domain.payload.request.SignupRequest;
import com.petproject.motelservice.repository.RolesRepository;
import com.petproject.motelservice.repository.UsersRepository;
import com.petproject.motelservice.security.jwt.JwtUtils;
import com.petproject.motelservice.security.services.UserDetailsImpl;

@RestController
@RequestMapping(value = "/user")
public class UserController {

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
	
	
	@GetMapping("/all")
//	@PreAuthorize("hasAuthority('ROLE_MODERATOR')")
	public ResponseEntity<?> registerUser() {
		return ResponseEntity.ok(usersRepository.findAll());
	}

	@PostMapping("/auth/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		

		return ResponseEntity.ok(jwt);
	}

	@PostMapping("/auth/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		if (usersRepository.existsByUsername(signUpRequest.getUserName())) {
			return ResponseEntity.badRequest().body("");
		}

		if (usersRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body("Error: Email is already in use!");
		}

		// Create new user's account
		Users user = new Users();
		user.setEmail(signUpRequest.getEmail());
		user.setActive(Boolean.TRUE);
		user.setUsername(signUpRequest.getUserName());
		user.setPassword(encoder.encode(signUpRequest.getPassword()));

		List<String> strRoles = signUpRequest.getRoles();
		List<Roles> roles = new ArrayList<>();

		if (strRoles == null) {
			Roles userRole = rolesRepository.findByName(ERoles.ROLE_LANDLORD)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
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
					default:
						Roles userRole = rolesRepository.findByName(ERoles.ROLE_TENANT)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
					}
			});
		}

		user.setRole(roles);
		usersRepository.save(user);

		return ResponseEntity.ok("User registered successfully!");
	}
	
	
}
