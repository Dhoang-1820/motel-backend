package com.petproject.motelservice.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.BookingDto;
import com.petproject.motelservice.domain.inventory.Booking;
import com.petproject.motelservice.repository.BookingRepositoty;
import com.petproject.motelservice.services.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired 
	BookingRepositoty bookingRepository;
	
	@Autowired 
	ModelMapper mapper;

	@Override
	public BookingDto saveBooking(BookingDto request) {
		Booking booking = mapper.map(request, Booking.class);
		booking = bookingRepository.save(booking);
		BookingDto result = mapper.map(booking, BookingDto.class);
		return result;
	}
	
}	
