package com.petproject.motelservice.services.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.BookingDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Booking;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.payload.Email;
import com.petproject.motelservice.repository.BookingRepositoty;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.services.BookingService;
import com.petproject.motelservice.services.MailService;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired 
	BookingRepositoty bookingRepository;
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	MailService mailService;
	
	@Autowired 
	ModelMapper mapper;

	@Override
	public BookingDto saveBooking(BookingDto request) {
		Booking booking = mapper.map(request, Booking.class);
		Rooms room = roomRepository.findById(request.getRoomId()).orElse(null);
		booking.setBookingDate(new Date());
		booking.setRoom(room);
		booking = bookingRepository.save(booking);
		BookingDto result = mapper.map(booking, BookingDto.class);
		sendOutNotification(booking);
		return result;
	}
	
	private void sendInvoice(String room, Accomodations accomodation, Booking booking) {
		Email email = new Email();
		 Users user = accomodation.getUser();
		try {
	        Map<String, Object> properties = new HashMap<>();
	        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm"); 
	        String bookingDate = dateFormat.format(booking.getBookingDate());
	        
	        email.setSubject("Motel service: Thông báo đặt phòng");
	        
	        properties.put("lanlord", user.getFirstname() + " " + user.getLastname());
	        properties.put("accomodationName", accomodation.getName());
	        properties.put("roomName", room);
	        properties.put("customer", booking.getName());
	        properties.put("customerEmail", booking.getEmail());
	        properties.put("customerPhone", booking.getPhone());
	        properties.put("bookingDate", bookingDate);
	        
	        email.setFrom("fromemail@gmail.com");
	        email.setTemplate("notification.html");
	        email.setProperties(properties);
	        
	        String regex = "^(.+)@(.+)$";
	        Pattern pattern = Pattern.compile(regex);
	       
	        Matcher matcher = pattern.matcher(user.getEmail());
			if (matcher.matches()) {
				email.setTo(user.getEmail());
				mailService.sendInvoiceEmail(email);
			}
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendOutNotification(Booking request) {
		Rooms room = roomRepository.findById(Integer.valueOf(request.getRoom().getId())).orElse(null);	
		Accomodations accomodations = room.getAccomodations();
		sendInvoice(room.getName(), accomodations, request);
	}
	
}	
