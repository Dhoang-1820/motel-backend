package com.petproject.motelservice.services;

import com.petproject.motelservice.domain.dto.BookingDto;

public interface BookingService {
	
	public BookingDto saveBooking(BookingDto request);
}
