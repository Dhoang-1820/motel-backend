package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.BookingDto;
import com.petproject.motelservice.domain.inventory.Booking;

public interface BookingService {
	
	BookingDto saveBooking(BookingDto request);
	
	void sendOutNotification(Booking request);
	
	List<BookingDto> getAllBookingByUserId(Integer userId);
}
