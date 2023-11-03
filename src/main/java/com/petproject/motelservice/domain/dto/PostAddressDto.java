package com.petproject.motelservice.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class PostAddressDto {

	private Integer provinceCode;
	
	private String province;
	
	List<DistrictDto> districts;
	
}
