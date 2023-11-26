package com.petproject.motelservice.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDto {
	
	private Integer id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String address;
	
	private String identifyNum;
	
	private String phone;
	
	private String imageUrl;
	
	private String username;
	
	private String password;
	
	private List<BankAccountDto> bankAccounts;
	
}
