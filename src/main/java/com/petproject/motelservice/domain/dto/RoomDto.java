package com.petproject.motelservice.domain.dto;

import lombok.Data;

@Data
public class RoomDto {

	private Integer id;

	private Integer capacity;
	
	private String name;
	
	private Double acreage;
	
	private Double price;
	
	private Boolean isRent = false;
	
	private Integer accomodationId;
}
