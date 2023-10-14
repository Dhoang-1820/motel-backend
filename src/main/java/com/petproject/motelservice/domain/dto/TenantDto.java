package com.petproject.motelservice.domain.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TenantDto {

	private Integer id;
	
	private String identifyNum;
	
	private String firstName;
	
	private String lastName;
	
	private Date startDate;
	
	private Boolean isStayed;
	
	private String imageUrl;
	
	private String phone;
	
	private String email;
	
	public TenantDto(Integer id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public TenantDto() { }
	
}
