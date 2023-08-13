package com.petproject.motelservice.controller;

import java.util.List;

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
	
	@PostMapping()
	public ResponseEntity<ApiResponse> saveRoom(@RequestBody RoomDto room) {
		final RoomDto result = roomService.saveRoom(room);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.GET_SUCESS_MSG));
	}
	
	@DeleteMapping("/{roomId}")
	public ResponseEntity<ApiResponse> saveRoom(@PathVariable Integer roomId) {
		roomService.removeRoom(roomId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, null, Constants.GET_SUCESS_MSG));
	}
	
	@PostMapping("/fee")
	public ResponseEntity<ApiResponse> saveRoomFee(@RequestBody RoomFeeRequest request) {
		roomFeeService.saveRoomFee(request);
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
