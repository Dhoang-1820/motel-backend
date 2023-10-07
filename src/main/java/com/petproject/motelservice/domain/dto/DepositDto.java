package com.petproject.motelservice.domain.dto;

import java.util.Date;

import com.petproject.motelservice.domain.payload.response.RoomResponse;

import lombok.Data;

@Data
public class DepositDto {
	
	private Integer id;
	
	private RoomResponse room;
	
	private Date dueDate;
	
	private Date startDate;
	
	private String note;
	
	private Double deposit;
	
	private Integer tenantId;
	
	private Boolean isActive;
	
	private Boolean isRepaid;
	
	private String firstName;
	
	private String lastName;
	
	private String phone;
	
	private String identifyNum;
	
	private String email;
	
	private Integer accomodationId;
	
}
