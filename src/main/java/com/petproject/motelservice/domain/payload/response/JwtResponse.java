package com.petproject.motelservice.domain.payload.response;

import com.petproject.motelservice.domain.inventory.EUserStatus;

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
	
	private String firstname;
	
	private String lastname;
	
	private String identifyNum;
	
	private String email;
	
	private String phone;
	
	private EUserStatus status;
	
	private String roles;
	
}
