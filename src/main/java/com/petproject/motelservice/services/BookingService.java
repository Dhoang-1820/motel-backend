package com.petproject.motelservice.services;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.domain.dto.BookingDto;
import com.petproject.motelservice.domain.inventory.Booking;
import com.petproject.motelservice.domain.inventory.Users;

public interface BookingService {
	
	BookingDto saveBooking(BookingDto request);
	
	List<BookingDto> getAllBookingByUserId(Integer userId);
	
	List<BookingDto> getBookingByDate(Integer userId, Date date);
	
	void deactivateBooking(Integer bookingId);
}
