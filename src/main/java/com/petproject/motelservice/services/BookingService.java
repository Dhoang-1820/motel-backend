package com.petproject.motelservice.services;

import com.petproject.motelservice.domain.dto.BookingDto;
import com.petproject.motelservice.domain.inventory.Booking;

public interface BookingService {
	
	public BookingDto saveBooking(BookingDto request);
	
	public void sendOutNotification(Booking request);
}
