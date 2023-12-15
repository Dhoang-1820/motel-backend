package com.petproject.motelservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.DepositDto;
import com.petproject.motelservice.domain.payload.request.CancelDepositRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.services.DepositService;
import com.petproject.motelservice.services.TenantsService;

@RestController
@RequestMapping(value = "/deposit")
public class DepositController {
	
	@Autowired
	DepositService depositService;
	
	@Autowired 
	TenantsService tenantsServices;
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getDepositByAccombodation(@PathVariable("id") Integer accomodationId) {
		final List<DepositDto> result = depositService.getDepositByAccomodation(accomodationId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping()
	public ResponseEntity<ApiResponse> saveDeposit(@RequestBody DepositDto request) {
		final Boolean result = depositService.saveDeposit(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PutMapping()
	public ResponseEntity<ApiResponse> cancelDeposit(@RequestBody CancelDepositRequest request) {
		final Boolean result = depositService.cancelDeposit(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}

}
