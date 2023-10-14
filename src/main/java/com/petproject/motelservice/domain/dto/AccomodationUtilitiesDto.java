package com.petproject.motelservice.domain.dto;

import lombok.Data;

@Data
public class AccomodationUtilitiesDto {
	
	private Integer accomodationId;
	
	private Integer id;

	private String name;
	
	private String unit;

	private Double price;
	
	private String description;
	
	private Boolean isDefault;
	
	private Integer quantity = 1;
}
