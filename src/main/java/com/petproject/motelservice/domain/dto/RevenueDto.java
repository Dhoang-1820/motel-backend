package com.petproject.motelservice.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class RevenueDto {

	private List<Integer> month;
	
	private List<Double> data;
}
