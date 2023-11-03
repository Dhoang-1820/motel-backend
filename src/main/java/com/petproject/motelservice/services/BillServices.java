package com.petproject.motelservice.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.petproject.motelservice.domain.dto.BillDto;
import com.petproject.motelservice.domain.dto.ElectricityWaterDto;
import com.petproject.motelservice.domain.dto.InvoiceDto;
import com.petproject.motelservice.domain.payload.request.ReturnRoomRequest;

public interface BillServices {
	
	List<BillDto> getMonthBillByAccomodation(Integer accomodationId, Date month);
	
	InvoiceDto updateInvoice(InvoiceDto request);
	
	List<InvoiceDto> getInvoiceByMonth(Integer accomodationId, Date month, Boolean isReturn);
	
	Boolean changePaymentStatus(Integer id);
	
	Boolean removeInvoice(Integer invoiceId);
	
	Boolean sendInvoice(Integer invoiceId);
	
	ElectricityWaterDto saveElectricWaterNum(ElectricityWaterDto request);

	List<ElectricityWaterDto> getElectricWaterNumByAccomodation(Integer accomodationId, Date month);
	
	Boolean checkIsRoomInputElectricWater(Integer accomodationId, Date month);

	List<InvoiceDto> issueInvoice(InvoiceDto request,  Boolean isReturn);

	InvoiceDto getInvoiceDetail(Integer invoiceId, Boolean isReturn);

	InvoiceDto getIssueInvoicePreview(Integer roomId, Date month);
	
	InvoiceDto getReturnRoomPreview(ReturnRoomRequest request);
	
	List<InvoiceDto> returnRoom(InvoiceDto request);

	Map<String, Boolean> checkIsReturnValid(Integer roomId, Date month);
	
}
