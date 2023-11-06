package com.petproject.motelservice.domain.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BookingDto {
	
	private Integer id;
	
	private String accomodation;
	
	private Integer accomodationId;
	
	private String name;
	
	private String email;
	
	private String phone;
	
	private Date reviewDate;
	
	private Date createdDate;
	
	private Integer roomId;
	
	private String room;
}
