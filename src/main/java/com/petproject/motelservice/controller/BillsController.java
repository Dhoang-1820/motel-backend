package com.petproject.motelservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.petproject.motelservice.domain.payload.request.IssueInvoiceRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.services.BillServices;

@RestController
@RequestMapping(value = "/invoice")
public class BillsController {

	@Autowired
	BillServices billServices;

	@PostMapping("/electric-water")
	public ResponseEntity<ApiResponse> getElectricWaterNumByAccomodation(@RequestBody BillRequest request) {
		final List<ElectricityWaterDto> result = billServices.getElectricWaterNumByAccomodation(request.getAccomodationId(), request.getMonth());
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
		final List<InvoiceDto> result = billServices.getInvoiceByMonth(request.getAccomodationId(), request.getMonth());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getInvoiceDetail(@PathVariable("id") Integer billId) {
		final InvoiceDto result = billServices.getInvoiceDetail(billId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/issue")
	public ResponseEntity<ApiResponse> issueInvoice(@RequestBody InvoiceDto request) {
		final List<InvoiceDto> result = billServices.issueInvoice(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/issue/preview")
	public ResponseEntity<ApiResponse> getPreviewIssueInvoice(@RequestBody IssueInvoiceRequest request) {
		final InvoiceDto result = billServices.getIssueInvoicePreview(request.getRoomId(), request.getMonth());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/send/{id}")
	public ResponseEntity<ApiResponse> sendInvoice(@PathVariable("id") Integer invoiceId) {
		Boolean result = billServices.sendInvoice(invoiceId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PutMapping("/confirm/{id}")
	public ResponseEntity<ApiResponse> confirmInvoicePayment(@PathVariable("id") Integer invoiceId) {
		final Boolean result = billServices.changePaymentStatus(invoiceId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<ApiResponse> removePayment(@PathVariable("id") Integer invoiceId) {
		final Boolean result = billServices.removeInvoice(invoiceId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
}
