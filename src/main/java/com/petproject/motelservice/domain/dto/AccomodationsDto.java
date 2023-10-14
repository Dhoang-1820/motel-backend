package com.petproject.motelservice.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class AccomodationsDto {
	
	private Integer id;
	
	private Integer addressId;

	private String addressLine;
	
	private String ward;

	private Integer wardCode;
	
	private String district;
	
	private Integer districtCode;
	
	private String province;
	
	private Integer provinceCode;

	private String name;

	private Boolean isActive;
	
	private Integer userId;
	
	private List<AccomodationUtilitiesDto> services;

}
