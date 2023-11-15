package com.petproject.motelservice.domain.payload.request;

import lombok.Data;

@Data
public class ConfirmInvoiceRequest {

	private Integer invoiceId;
	
	private Double paidMoney;
	
	private Double debt;
}
