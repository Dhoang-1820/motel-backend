package com.petproject.motelservice.domain.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	
	private String token;
	
	private String refreshToken;
	
	private Integer id;
	
	private String username;
	
	private String email;
	
	private Boolean isActive;
	
	private String roles;
	
}
