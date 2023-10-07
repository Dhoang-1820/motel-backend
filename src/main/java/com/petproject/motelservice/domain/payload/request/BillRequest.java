package com.petproject.motelservice.domain.payload.request;

import java.util.Date;

import lombok.Data;

@Data
public class BillRequest {
	
	private Integer accomodationId;
	
	private Date month;

}
