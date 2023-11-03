package com.petproject.motelservice.domain.query.response;

import java.util.Date;

public interface InvoiceResponse {
	
	Integer getRoomId();

	String getRoomName();
	
	Date getBillDate();

	Double getTotalPayment();

	Double getPaidMoney();

	Double getDebt();

	Date getPaymentDate();

	Boolean getIsPay();
	
	Double getDiscount();

	String getRepresentative();
	
	Integer getBillId();
	
	Integer getQuantitySent();
	
	Date getReturnDate();
	
	Double getTotalPrice();

}
