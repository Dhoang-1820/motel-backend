package com.petproject.motelservice.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.petproject.motelservice.domain.dto.ElectricityWaterDto;
import com.petproject.motelservice.domain.dto.InvoiceDto;
import com.petproject.motelservice.domain.payload.request.ConfirmInvoiceRequest;
import com.petproject.motelservice.domain.payload.request.ReturnRoomRequest;
import com.petproject.motelservice.domain.query.response.InvoiceResponse;

public interface BillServices {
	
	InvoiceDto updateInvoice(InvoiceDto request);
	
	List<InvoiceDto> getInvoiceByMonth(Integer accomodationId, Date month, Boolean isReturn);
	
	Boolean confirmPayment(ConfirmInvoiceRequest request);
	
	Boolean removeInvoice(Integer invoiceId);
	
	void sendInvoiceEmail(Integer invoiceId, Date month);
	
	ElectricityWaterDto saveElectricWaterNum(ElectricityWaterDto request);

	List<ElectricityWaterDto> getElectricWaterNumByAccomodation(Integer accomodationId, Date month);
	
	ElectricityWaterDto getElectricWaterNumByMonthAndRoom(Integer roomId, Date month);
	
	Boolean checkIsCanRemoveEletricWater(Integer id, Date month);
	
	Boolean removeEletricWater(Integer id);
	
	Boolean checkIsRoomInputElectricWater(Integer accomodationId, Date month);

	List<InvoiceDto> issueInvoice(InvoiceDto request,  Boolean isReturn);

	InvoiceDto getInvoiceDetail(Integer invoiceId, Date month, Boolean isReturn);

	InvoiceDto getIssueInvoicePreview(Integer roomId, Date month);
	
	InvoiceDto getReturnRoomPreview(ReturnRoomRequest request);
	
	List<InvoiceDto> returnRoom(InvoiceDto request);

	Map<String, Boolean> checkIsReturnValid(Integer roomId, Date month);

	List<InvoiceDto> issueInvoiceByRoomId(Integer roomId, Date month);

	List<InvoiceResponse> getInvoice(Integer accomodationId, Date month);

	Boolean checkIsEditElectricWater(Integer roomId, Date month);
	
}
