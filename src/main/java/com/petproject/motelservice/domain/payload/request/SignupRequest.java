package com.petproject.motelservice.domain.payload.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SignupRequest {
	
	private String userName;
	
	private String email;
	
	private String password;
	
	private List<String> roles;
}
