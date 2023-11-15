package com.petproject.motelservice.domain.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserPreferenceDto {
	
	private Integer id;

	private Date issueInvoiceDate;
	
	private Date electricWaterDate;
	
	private Integer userId;
}
