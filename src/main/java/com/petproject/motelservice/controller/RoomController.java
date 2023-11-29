package com.petproject.motelservice.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.petproject.motelservice.domain.dto.RoomDto;
import com.petproject.motelservice.domain.payload.request.BillRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.services.RoomService;

@RestController
@RequestMapping(value = "/room")
public class RoomController {
	
	@Autowired
	RoomService roomService;
	
	@GetMapping("/accomodations/{id}")
	public ResponseEntity<ApiResponse> getRoomByAccomodationId(@PathVariable Integer id) {
		final List<RoomDto> result = roomService.getRoomsByAccomodation(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getRoomId(@PathVariable Integer id) {
		final RoomResponse result = roomService.getRoomById(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/utility/{id}")
	public ResponseEntity<ApiResponse> getRoomDropdownByAccomodationId(@PathVariable Integer id) {
		final List<RoomResponse> result = roomService.getRoomDropDown(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/utility/duplicated/{accomodationId}")
	public ResponseEntity<ApiResponse> checkDuplicateName(@RequestBody String roomName, @PathVariable Integer accomodationId) {
		final Boolean result = roomService.isDuplicateRoom(roomName, accomodationId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/utility/no-deposit/{id}")
	public ResponseEntity<ApiResponse> getRoomNoDeposit(@PathVariable Integer id) {
		final List<RoomResponse> result = roomService.getRoomNoDeposit(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/utility/no-rent/{id}")
	public ResponseEntity<ApiResponse> getRoomNoRented(@PathVariable Integer id) {
		final List<RoomResponse> result = roomService.getRoomNoRented(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/utility/no-post/{id}")
	public ResponseEntity<ApiResponse> getRoomNoPost(@PathVariable Integer id) {
		final List<RoomResponse> result = roomService.getRoomNoPost(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/utility/no-post/no-deposit/{id}")
	public ResponseEntity<ApiResponse> getRoomNoPostAndDeposit(@PathVariable Integer id) {
		final List<RoomResponse> result = roomService.getRoomNoPostAndDeposit(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/utility/rented/{id}")
	public ResponseEntity<ApiResponse> getRoomRented(@PathVariable Integer id) {
		final List<RoomResponse> result = roomService.getRoomRented(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/utility/rented-date/{id}")
	public ResponseEntity<ApiResponse> getRoomRentedDate(@PathVariable Integer id) {
		final Map<String, Date> result = roomService.getRoomRentedDate(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/utility/no-electric-water")
	public ResponseEntity<ApiResponse> getRoomNoElectricWater(@RequestBody BillRequest request) {
		final List<RoomResponse> result = roomService.getRoomNoElectricWaterIndex(request.getId(), request.getMonth());
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping()
	public ResponseEntity<ApiResponse> checkIsRoomHasDeposit(@RequestParam("roomId") Integer id) {
		final Map<String, Object> result = roomService.checkIsRoomHasDeposit(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping()
	public ResponseEntity<ApiResponse> saveRoom(@RequestBody RoomDto room) {
		final List<RoomDto> result = roomService.saveRoom(room);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@DeleteMapping("/{roomId}")
	public ResponseEntity<ApiResponse> removeRoom(@PathVariable Integer roomId) {
		roomService.removeRoom(roomId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
	}
	
}
