package com.petproject.motelservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.BillDto;
import com.petproject.motelservice.domain.dto.InvoiceDto;
import com.petproject.motelservice.domain.payload.request.BillRequest;
import com.petproject.motelservice.domain.payload.request.SendInvoiceRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.services.BillServices;

@RestController
@RequestMapping(value = "/bill")
public class BillsController {

	@Autowired
	BillServices billServices;

	@PostMapping()
	public ResponseEntity<ApiResponse> getBillMonthByAccomodation(@RequestBody BillRequest request) {
		final List<BillDto> result = billServices.getMonthBillByAccomodation(request.getAccomodationId(), request.getMonth());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/save")
	public ResponseEntity<ApiResponse> saveBill(@RequestBody BillDto request) {
		final BillDto result = billServices.createOrUpdate(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/invoice")
	public ResponseEntity<ApiResponse> getMonthInvoiceByAccomodation(@RequestBody BillRequest request) {
		final List<InvoiceDto> result = billServices.getMonthInvoiceByAccomodation(request.getAccomodationId(), request.getMonth());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> changePayStatus(@PathVariable("id") Integer billId) {
		billServices.changePaymentStatus(billId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
	}
	
	@PutMapping()
	public ResponseEntity<ApiResponse> sendInvoice(@RequestBody SendInvoiceRequest request) {
		billServices.sendInvoice(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
	}
	
}
