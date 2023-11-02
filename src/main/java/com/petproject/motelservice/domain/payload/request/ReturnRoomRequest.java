package com.petproject.motelservice.domain.payload.request;

import java.util.Date;

import lombok.Data;

@Data
public class ReturnRoomRequest {
	
	private Integer roomId;
	
	private Date returnDate;
	
}
