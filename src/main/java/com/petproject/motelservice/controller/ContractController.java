package com.petproject.motelservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.ContractDto;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.services.UserContractService;

@RestController
@RequestMapping(value = "/contract")
public class ContractController {
	
	@Autowired
	UserContractService userContractService;
	
	@GetMapping("/accomodation/{id}")
	public ResponseEntity<ApiResponse> getContractByAccomodation(@PathVariable Integer id) {
		final List<ContractDto> result = userContractService.getContractByAccomodation(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping()
	public ResponseEntity<ApiResponse> saveContract(@RequestBody ContractDto request) {
		final List<ContractDto> result = userContractService.saveContract(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}

}
