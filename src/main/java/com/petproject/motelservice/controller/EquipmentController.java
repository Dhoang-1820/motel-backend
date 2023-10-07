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
import com.petproject.motelservice.domain.dto.EquipmentDto;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.services.EquipmentService;

@RestController
@RequestMapping(value = "/equipment")
public class EquipmentController {
	
	@Autowired
	EquipmentService equipmentService;
	
	@GetMapping("/accomodation/{id}")
	public ResponseEntity<ApiResponse> getByAccomodation(@PathVariable("id") Integer accomodationId) {
		final List<EquipmentDto> result = equipmentService.getByAccomodationId(accomodationId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/room/{id}")
	public ResponseEntity<ApiResponse> getByRoom(@PathVariable("id") Integer roomId) {
		final List<EquipmentDto> result = equipmentService.getByRoomId(roomId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping()
	public ResponseEntity<ApiResponse> getByName(@RequestParam("name") String name) {
		final List<EquipmentDto> result = equipmentService.getByName(name);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping()
	public ResponseEntity<ApiResponse> saveEquipment(@RequestBody List<EquipmentDto> request) {
		final List<EquipmentDto> result = equipmentService.saveEquipment(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.CREATE_SUCCESS_MSG));
	}
	
	@DeleteMapping()
	public ResponseEntity<ApiResponse> deteleEquipment(@PathVariable("id") Integer id) {
		equipmentService.deleteEquipment(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.DELETE_SUCCESS_MSG));
	}
	
}
