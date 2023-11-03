package com.petproject.motelservice.domain.payload.request;

import java.util.Date;

import lombok.Data;

@Data
public class IssueInvoiceRequest {
	
	private Integer roomId;
	
	private Date month;
}
