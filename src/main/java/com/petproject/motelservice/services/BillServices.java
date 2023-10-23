package com.petproject.motelservice.services;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.domain.dto.BillDto;
import com.petproject.motelservice.domain.dto.ElectricityWaterDto;
import com.petproject.motelservice.domain.dto.InvoiceDto;

public interface BillServices {
	
	List<BillDto> getMonthBillByAccomodation(Integer accomodationId, Date month);
	
	InvoiceDto updateInvoice(InvoiceDto request);
	
	List<InvoiceDto> getInvoiceByMonth(Integer accomodationId, Date month);
	
	Boolean changePaymentStatus(Integer id);
	
	Boolean removeInvoice(Integer invoiceId);
	
	Boolean sendInvoice(Integer invoiceId);
	
	ElectricityWaterDto saveElectricWaterNum(ElectricityWaterDto request);

	List<ElectricityWaterDto> getElectricWaterNumByAccomodation(Integer accomodationId, Date month);

	List<InvoiceDto> issueInvoice(InvoiceDto request);

	InvoiceDto getInvoiceDetail(Integer invoiceId);

	InvoiceDto getIssueInvoicePreview(Integer roomId, Date month);
	
}
