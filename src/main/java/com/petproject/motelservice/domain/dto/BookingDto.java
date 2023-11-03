package com.petproject.motelservice.domain.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BookingDto {
	
	private Integer id;
	
	private String name;
	
	private String email;
	
	private String phone;
	
	private Date reviewDate;
	
	private Integer roomId;
}
