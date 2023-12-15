package com.petproject.motelservice.domain.payload.request;

import com.petproject.motelservice.domain.inventory.ERoles;
import com.petproject.motelservice.domain.inventory.EUserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SignupRequest {
	
	private String userName;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String phone;
	
	private String identifyNum;
	
	private String address;
	
	private EUserStatus status;
	
	private ERoles roles;
}
