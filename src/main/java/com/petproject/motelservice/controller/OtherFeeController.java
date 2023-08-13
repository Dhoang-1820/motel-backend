package com.petproject.motelservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.OtherFeesDto;
import com.petproject.motelservice.domain.dto.RoomServiceDto;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.domain.query.response.OtherFeesResponse;
import com.petproject.motelservice.services.OtherFeeService;
import com.petproject.motelservice.services.RoomFeeService;

@RestController
@RequestMapping(value = "/other-fee")
public class OtherFeeController {
	
	@Autowired
	OtherFeeService otherFeeService;
	
	@Autowired
	RoomFeeService feeService;
	
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
	
	@GetMapping("/accomodation/{id}")
	public ResponseEntity<ApiResponse> getRoomFeeByAccomodation(@PathVariable Integer id) {
		final List<RoomServiceDto> result = feeService.getRoomFeeByAccomodation(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping()
	public ResponseEntity<ApiResponse> getRemainFee(@RequestParam("room") Integer roomId, @RequestParam("accomodation") Integer accomodationId) {
		final List<OtherFeesResponse> result = feeService.getRemainFeesDropDown(roomId, accomodationId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
}
