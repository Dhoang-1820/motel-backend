package com.petproject.motelservice.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class AccomodationsDto {
	
	private Integer id;

	private Double electricPrice;

	private Double waterPrice;

	private String address;

	private String name;

	private Boolean isActive;
	
	private Integer userId;

	private List<OtherFeesDto> otherFees;
}
