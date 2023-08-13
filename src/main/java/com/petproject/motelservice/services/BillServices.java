package com.petproject.motelservice.services;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.domain.dto.BillDto;
import com.petproject.motelservice.domain.dto.InvoiceDto;
import com.petproject.motelservice.domain.payload.request.SendInvoiceRequest;

public interface BillServices {
	
	public List<BillDto> getMonthBillByAccomodation(Integer accomodationId, Date month);
	
	public BillDto createOrUpdate(BillDto request);
	
	public List<InvoiceDto> getMonthInvoiceByAccomodation(Integer accomodationId, Date month);
	
	public void changePaymentStatus(Integer id);
	
	public void  sendInvoice( SendInvoiceRequest request);
}
