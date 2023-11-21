package com.petproject.motelservice.domain.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@Data
public class DropDownAccomodation {
	
	private Integer id;
	
	private String name;
	
	private String address;
}
