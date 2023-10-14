package com.petproject.motelservice.services;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.domain.dto.BillDto;
import com.petproject.motelservice.domain.dto.ElectricityWaterDto;
import com.petproject.motelservice.domain.dto.InvoiceDto;
import com.petproject.motelservice.domain.payload.request.SendInvoiceRequest;

public interface BillServices {
	
	List<BillDto> getMonthBillByAccomodation(Integer accomodationId, Date month);
	
	BillDto createOrUpdate(BillDto request);
	
	List<InvoiceDto> getMonthInvoiceByAccomodation(Integer accomodationId, Date month);
	
	void changePaymentStatus(Integer id);
	
	void  sendInvoice( SendInvoiceRequest request);
	
	ElectricityWaterDto saveElectricWaterNum(ElectricityWaterDto request);

	List<ElectricityWaterDto> getElectricWaterNumByAccomodation(Integer accomodationId, Date month);
}
