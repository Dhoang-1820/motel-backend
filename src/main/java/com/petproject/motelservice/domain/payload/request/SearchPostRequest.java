package com.petproject.motelservice.domain.payload.request;

import lombok.Data;

@Data
public class SearchPostRequest {
	
	private SearchByAddressRequest address;
	
	private RangeRequest price;
	
	private RangeRequest areage;
}
