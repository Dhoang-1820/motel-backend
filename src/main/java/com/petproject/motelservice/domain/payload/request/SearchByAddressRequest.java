package com.petproject.motelservice.domain.payload.request;

import lombok.Data;

@Data
public class SearchByAddressRequest {

	private String name;
	
	private Integer code;
	
	private Integer level;
	
	private Integer parentCode;
	
}
