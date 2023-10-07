package com.petproject.motelservice.domain.payload.request;

import java.util.Date;

import lombok.Data;


@Data
public class SendInvoiceRequest {
	
	private Integer roomId;
	
	private Integer billId;
	
	private Date month;
}
