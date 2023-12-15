package com.petproject.motelservice.domain.payload.request;

import java.util.Date;

import lombok.Data;

@Data
public class DashboardRequest {

	private Integer userId;
	
	private Date year;
}
