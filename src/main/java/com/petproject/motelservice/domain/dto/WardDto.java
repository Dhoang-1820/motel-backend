package com.petproject.motelservice.domain.dto;

import lombok.Data;

@Data
public class WardDto {

	private String name;

	private Integer code;
	
	private Integer level = 3;
	
	private Integer parentCode;

}
