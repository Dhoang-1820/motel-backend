package com.petproject.motelservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petproject.motelservice.common.Constants;
import com.petproject.motelservice.domain.dto.BookingDto;
import com.petproject.motelservice.domain.payload.response.ApiResponse;
import com.petproject.motelservice.services.BookingService;

@RestController
@RequestMapping(value = "/booking")
public class BookingController {
	
	@Autowired
	BookingService bookingService;

	@PostMapping()
	public ResponseEntity<ApiResponse> saveBooking(@RequestBody BookingDto bookingDto) {
		final BookingDto result = bookingService.saveBooking(bookingDto);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, result, Constants.CREATE_SUCCESS_MSG));
	}
}	
