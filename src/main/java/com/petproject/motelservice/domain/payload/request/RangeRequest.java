package com.petproject.motelservice.domain.payload.request;

import lombok.Data;

@Data
public class RangeRequest {

	private Double from;
	
	private Double to;
}
