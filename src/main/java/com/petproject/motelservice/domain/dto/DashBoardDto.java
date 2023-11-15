package com.petproject.motelservice.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class DashBoardDto {

	private Integer accomodationNum;
	
	private Integer roomNum;
	
	private Integer emptyRoomNum;
	
	private Integer TenantNum;
	
	private List<String> notifications;
	
}
