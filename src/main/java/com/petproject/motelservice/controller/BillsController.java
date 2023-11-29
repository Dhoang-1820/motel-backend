package com.petproject.motelservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.ElectricityWaterDto;
import com.petproject.motelservice.domain.dto.InvoiceDto;
import com.petproject.motelservice.domain.payload.request.BillRequest;
import com.petproject.motelservice.domain.payload.request.ConfirmInvoiceRequest;
import com.petproject.motelservice.domain.payload.request.IssueInvoiceRequest;
import com.petproject.motelservice.domain.payload.request.ReturnRoomRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.services.BillServices;

@RestController
@RequestMapping(value = "/invoice")
public class BillsController {

	@Autowired
	BillServices billServices;

	@PostMapping("/electric-water")
	public ResponseEntity<ApiResponse> getElectricWaterNumByAccomodation(@RequestBody BillRequest request) {
		final List<ElectricityWaterDto> result = billServices.getElectricWaterNumByAccomodation(request.getId(), request.getMonth());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/room/electric-water")
	public ResponseEntity<ApiResponse> getElectricWaterByMonthAndRoom(@RequestBody BillRequest request) {
		final ElectricityWaterDto result = billServices.getElectricWaterNumByMonthAndRoom(request.getId(), request.getMonth());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/check/electric-water")
	public ResponseEntity<ApiResponse> checkRemoveEletricWater(@RequestBody IssueInvoiceRequest request) {
		final Boolean result = billServices.checkIsCanRemoveEletricWater(request.getRoomId(), request.getMonth());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@DeleteMapping("/remove/electric-water/{id}")
	public ResponseEntity<ApiResponse> removeEletricWater(@PathVariable Integer id) {
		final Boolean result = billServices.removeEletricWater(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
		
	@PutMapping()
	public ResponseEntity<ApiResponse> updateInvoice(@RequestBody InvoiceDto request) {
		final InvoiceDto result = billServices.updateInvoice(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/electric-water/save")
	public ResponseEntity<ApiResponse> saveElectricityWater(@RequestBody ElectricityWaterDto request) {
		final ElectricityWaterDto result = billServices.saveElectricWaterNum(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping()
	public ResponseEntity<ApiResponse> getMonthInvoiceByAccomodation(@RequestBody BillRequest request) {
		final List<InvoiceDto> result = billServices.getInvoiceByMonth(request.getId(), request.getMonth(), request.getIsReturn());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/detail")
	public ResponseEntity<ApiResponse> getInvoiceDetail(@RequestBody BillRequest request) {
		final InvoiceDto result = billServices.getInvoiceDetail(request.getId(), request.getMonth(), false);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/return/detail")
	public ResponseEntity<ApiResponse> getReturnInvoiceDetail(@RequestBody BillRequest request) {
		final InvoiceDto result = billServices.getInvoiceDetail(request.getId(), request.getMonth(), true);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/issue")
	public ResponseEntity<ApiResponse> issueInvoice(@RequestBody InvoiceDto request) {
		final List<InvoiceDto> result = billServices.issueInvoice(request, false);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/issue/preview")
	public ResponseEntity<ApiResponse> getPreviewIssueInvoice(@RequestBody IssueInvoiceRequest request) {
		final InvoiceDto result = billServices.getIssueInvoicePreview(request.getRoomId(), request.getMonth());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/send")
	public ResponseEntity<ApiResponse> sendInvoice(@RequestBody BillRequest request) {
		billServices.sendInvoiceEmail(request.getId(), request.getMonth());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/room")
	public ResponseEntity<ApiResponse> checkReturnValid(@RequestBody BillRequest request) {
		Map<String, Boolean> result = billServices.checkIsReturnValid(request.getId(), request.getMonth());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PutMapping("/confirm")
	public ResponseEntity<ApiResponse> confirmInvoicePayment(@RequestBody ConfirmInvoiceRequest request) {
		final Boolean result = billServices.confirmPayment(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/return")
	public ResponseEntity<ApiResponse> returnRoom(@RequestBody ReturnRoomRequest request) {
		final InvoiceDto result = billServices.getReturnRoomPreview(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/return/issue")
	public ResponseEntity<ApiResponse> issueInvoiceReturn(@RequestBody InvoiceDto request) {
		final List<InvoiceDto> result = billServices.returnRoom(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<ApiResponse> removePayment(@PathVariable("id") Integer invoiceId) {
		final Boolean result = billServices.removeInvoice(invoiceId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
}
