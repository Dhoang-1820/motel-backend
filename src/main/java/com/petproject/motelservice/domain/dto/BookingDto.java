package com.petproject.motelservice.domain.dto;

import java.util.Date;

import com.petproject.motelservice.domain.payload.response.RoomResponse;

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
	
	private RoomResponse room;
	
	private Integer roomId;
	
}
