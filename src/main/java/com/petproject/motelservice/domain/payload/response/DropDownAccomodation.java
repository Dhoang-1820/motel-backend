package com.petproject.motelservice.domain.payload.response;

import lombok.Data;

@Data
public class DropDownAccomodation {
	
	private Integer id;
	
	private String name;
	
	private Double waterPrice;
	
	private Integer electricPrice;
}
