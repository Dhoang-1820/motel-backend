package com.petproject.motelservice.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.RoomDto;
import com.petproject.motelservice.domain.dto.RoomImageDto;
import com.petproject.motelservice.domain.payload.request.BillRequest;
import com.petproject.motelservice.domain.payload.request.RoomFeeRequest;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.domain.query.response.RoomServiceResponse;
import com.petproject.motelservice.services.RoomFeeService;
import com.petproject.motelservice.services.RoomService;

@RestController
@RequestMapping(value = "/room")
public class RoomController {
	
	@Autowired
	RoomService roomService;
	
	@Autowired
	RoomFeeService roomFeeService;
	
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
	
	@GetMapping("/utility/no-service/{id}")
	public ResponseEntity<ApiResponse> getRoomNotHasServiceByAccomodation(@PathVariable Integer id) {
		final List<RoomServiceResponse> result = roomService.getRoomNotHasService(id);
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
	
	@PostMapping("/fee")
	public ResponseEntity<ApiResponse> saveRoomFee(@RequestBody RoomFeeRequest request) {
		roomFeeService.saveRoomFee(request);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
	}
	
	@DeleteMapping("/fee/{roomId}/{feeId}")
	public ResponseEntity<ApiResponse> removeRoomFee(@PathVariable("roomId") Integer roomId, @PathVariable("feeId") Integer feeId) {
		roomFeeService.removeRoomFee(roomId, feeId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/image/{roomId}")
	public ResponseEntity<ApiResponse> saveRoomImage(@RequestParam("file") MultipartFile[] files, @PathVariable("roomId") Integer roomId) {
		roomService.saveRoomImage(files, roomId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
	}
	
	@GetMapping("/image/{roomId}")
	public ResponseEntity<ApiResponse> getImagesByRoom(@PathVariable("roomId") Integer roomId) {
		final RoomImageDto result = roomService.getRoomImages(roomId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@PutMapping("/image/{imgId}")
	public ResponseEntity<ApiResponse> changeImageRoom(@RequestParam("file") MultipartFile[] files, @PathVariable("imgId") Integer imgId) {
		roomService.changeRoomImage(files, imgId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
	}
	
	@DeleteMapping("/image/{roomId}")
	public ResponseEntity<ApiResponse> removeImage(@PathVariable("roomId") Integer roomId) {
		roomService.removeImage(roomId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
	}
}
