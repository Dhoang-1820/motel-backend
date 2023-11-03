package com.petproject.motelservice.domain.dto;

import lombok.Data;

@Data
public class BankAccountDto {
	
	private Integer id;

	private String bankNumber;
	
	private String bankName;
	
	private String accountOwner;
	
	private Integer userId;
}
