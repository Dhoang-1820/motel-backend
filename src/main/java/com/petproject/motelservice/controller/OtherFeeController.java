package com.petproject.motelservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.OtherFeesDto;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.services.OtherFeeService;

@RestController
@RequestMapping(value = "/other-fee")
public class OtherFeeController {
	
	@Autowired
	OtherFeeService otherFeeService;
	
	@PostMapping()
	public ResponseEntity<ApiResponse> saveOtherFee(@RequestBody OtherFeesDto feesDto) {
		final OtherFeesDto result = otherFeeService.createOrUpdate(feesDto);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.CREATE_SUCCESS_MSG));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> removeOtherFee(@PathVariable Integer id) {
		otherFeeService.removeFee(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true, null, Constants.DELETE_SUCCESS_MSG));
	}
}
