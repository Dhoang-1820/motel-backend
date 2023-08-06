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
import org.springframework.web.bind.annotation.RestController;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.AccomodationsDto;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.domain.payload.response.DropDownAccomodation;
import com.petproject.motelservice.services.AccomodationService;

@RestController
@RequestMapping(value = "/accomodation")
public class AccomodationController {
	
	@Autowired 
	AccomodationService accomodationService;

	@GetMapping()
	public ResponseEntity<ApiResponse> registerUser() {
		final List<AccomodationsDto> result = accomodationService.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse> getAccomodationByUserId(@PathVariable Integer userId) {
		final List<AccomodationsDto> result = accomodationService.getAccomodationByUserId(userId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/utility/{userId}")
	public ResponseEntity<ApiResponse> getDropdownAccomodationByUserId(@PathVariable Integer userId) {
		final List<DropDownAccomodation> result = accomodationService.getDropdownAccomodationByUserId(userId);	
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping()
	public ResponseEntity<ApiResponse> saveAccomodation(@RequestBody AccomodationsDto accomodations) {
		final AccomodationsDto result = accomodationService.createOrUpdate(accomodations);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.CREATE_SUCCESS_MSG));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> removeOtherFee(@PathVariable Integer id) {
		accomodationService.removeAccomodation(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true, null, Constants.DELETE_SUCCESS_MSG));
	}
}
