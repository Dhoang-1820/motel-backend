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
import com.petproject.motelservice.domain.dto.TenantDto;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.services.TenantsService;

@RestController
@RequestMapping(value = "/tenant")
public class TenantsController {
	
	@Autowired
	TenantsService tenantsServices;
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getTenantByAccomodation(@PathVariable Integer id) {
		final List<TenantDto> result = tenantsServices.getTenantByAccomodation(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/deposit/{id}")
	public ResponseEntity<ApiResponse> getTenantWithoutDeposit(@PathVariable Integer id) {
		final List<TenantDto> result = tenantsServices.getTenantNotDeposit(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/contract/{id}")
	public ResponseEntity<ApiResponse> getTenantWithoutContract(@PathVariable Integer id) {
		final List<TenantDto> result = tenantsServices.getTenantNotContract(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping()
	public ResponseEntity<ApiResponse> saveTenant(@RequestBody TenantDto tenant) {
		final TenantDto result = tenantsServices.createOrUpdate(tenant);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.CREATE_SUCCESS_MSG));
	}
	
	@PostMapping("/duplicated")
	public ResponseEntity<ApiResponse> checkDuplicated(@RequestBody String identifyNum) {
		final Boolean result = tenantsServices.checkDuplicated(identifyNum);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.CREATE_SUCCESS_MSG));
	}
}
