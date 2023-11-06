package com.petproject.motelservice.domain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PostAddressDto {

	private Integer code;
	
	private String name;
	
	private Integer level = 1;
	
	List<DistrictDto> districts;
	
}
