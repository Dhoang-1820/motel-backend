package com.petproject.motelservice.domain.payload.request;

import com.petproject.motelservice.domain.inventory.ERoles;

import lombok.Data;

@Data
public class UpdateUserRequest {
	
	private Integer userId;
	
	private String firstName;
	
	private String lastName;
	
	private String identifyNum;
	
	private Boolean active;
	
	private String userName;
	
	private String password;
	
	private String email;
	
	private String phone;
	
	private String address;
	
	private ERoles role;
}
