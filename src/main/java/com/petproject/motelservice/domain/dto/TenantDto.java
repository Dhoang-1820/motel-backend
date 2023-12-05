package com.petproject.motelservice.domain.dto;

import java.util.Date;

import com.petproject.motelservice.domain.inventory.EGender;

import lombok.Data;

@Data
public class TenantDto {

	private Integer id;
	
	private String identifyNum;
	
	private String firstName;
	
	private String lastName;
	
	private Date startDate;
	
	private Date endDate;
	
	private Boolean isStayed;
	
	private EGender gender;
	
	private String phone;
	
	private String email;
	
	private Integer accomodationId;

	public TenantDto() { }

	public TenantDto(Integer id, String identifyNum, String firstName, String lastName, String phone) {
		super();
		this.id = id;
		this.identifyNum = identifyNum;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
	}
	
}
