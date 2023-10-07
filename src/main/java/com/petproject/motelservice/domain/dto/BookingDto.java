package com.petproject.motelservice.domain.dto;

import lombok.Data;

@Data
public class BookingDto {
	
	private Integer id;
	
	private String name;
	
	private String email;
	
	private String phone;
	
	private Integer roomId;
}
