package com.petproject.motelservice.domain.dto;

import java.util.Date;

import com.petproject.motelservice.domain.payload.response.RoomResponse;

import lombok.Data;

@Data
public class TenantDto {

	private Integer id;
	
	private Integer identifyNum;
	
	private String firstName;
	
	private String lastName;
	
	private Date startDate;
	
	private Boolean isStayed;
	
	private String imageUrl;
	
	private String phone;
	
	private String email;
	
	private RoomResponse room;
}
