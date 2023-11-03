package com.petproject.motelservice.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class DistrictDto {

	private Integer districtCode;
	
	private String district;
	
	private List<WardDto> districts;
}
