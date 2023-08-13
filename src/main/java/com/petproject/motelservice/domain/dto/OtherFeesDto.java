package com.petproject.motelservice.domain.dto;

import lombok.Data;

@Data
public class OtherFeesDto {
	
	private Integer accomodationId;
	
	private Integer id;

	private String name;
	
	private String unit;

	private Double price;
}
