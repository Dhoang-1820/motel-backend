package com.petproject.motelservice.services.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.petproject.motelservice.domain.payload.response.RoomResponse;
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
		Booking booking = new Booking();
		booking.setEmail(request.getEmail());
		booking.setName(request.getName());
		booking.setPhone(request.getPhone());
		booking.setReviewDate(request.getReviewDate());
		Rooms room = roomRepository.findById(request.getRoomId()).orElse(null);
		booking.setBookingDate(new Date());
		booking.setRoom(room);
		booking.setIsActive(Boolean.TRUE);
		booking = bookingRepository.save(booking);
		BookingDto result = convert2Dto(booking);
		sendOutNotification(booking);
		return result;
	}

	@Override
	public List<BookingDto> getAllBookingByUserId(Integer userId) {
		List<BookingDto> result = new ArrayList<>();
		List<Booking> booking = bookingRepository.findBookingByUserId(userId);
		for (Booking item : booking) {
			result.add(convert2Dto(item));
		}
		return result;
	}

	private BookingDto convert2Dto(Booking booking) {
		BookingDto dto = new BookingDto();
		dto.setId(booking.getId());
		dto.setAccomodation(booking.getRoom().getAccomodations().getName());
		dto.setAccomodationId(booking.getRoom().getAccomodations().getId());
		dto.setCreatedDate(booking.getBookingDate());
		dto.setEmail(booking.getEmail());
		dto.setName(booking.getName());
		dto.setPhone(booking.getPhone());
		dto.setReviewDate(booking.getBookingDate());
		Rooms room = booking.getRoom();
		dto.setRoom(new RoomResponse(room.getId(), room.getName(), room.getPrice(), room.getCapacity()));
		dto.setRoomId(booking.getRoom().getId());
		return dto;
	}
	
	@Override
	public List<BookingDto> getBookingByDate(Integer userId, Date date) {
		List<BookingDto> result = new ArrayList<>();
		List<Booking> booking = bookingRepository.findBookingByUserIdAndDate(userId, date);
		for (Booking item : booking) {
			result.add(convert2Dto(item));
		}
		return result;
	}

	private void sendBookingNotification(String room, Accomodations accomodation, Booking booking) {
		Email email = new Email();
		Users user = accomodation.getUser();
		try {
			Map<String, Object> properties = new HashMap<>();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
			String bookingDate = dateFormat.format(booking.getBookingDate());
			String reviewDate = null;
			if (booking.getReviewDate() != null) {
				reviewDate = dateFormat.format(booking.getReviewDate());				
			}

			email.setSubject("Motel service: Thông báo đặt phòng");

			properties.put("lanlord", user.getFirstname() + " " + user.getLastname());
			properties.put("accomodationName", accomodation.getName());
			properties.put("roomName", room);
			properties.put("customer", booking.getName());
			properties.put("customerEmail", booking.getEmail());
			properties.put("customerPhone", booking.getPhone());
			properties.put("bookingDate", bookingDate);
			if (reviewDate != null) {
				properties.put("reviewDate", reviewDate);				
			}

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
		Rooms room = roomRepository.findById(request.getRoom().getId()).orElse(null);
		Accomodations accomodations = room.getAccomodations();
		sendBookingNotification(room.getName(), accomodations, request);
	}

	@Override
	public void deactivateBooking(Integer bookingId) {
		Booking booking = bookingRepository.findById(bookingId).orElse(null);
		booking.setIsActive(Boolean.FALSE);
		bookingRepository.save(booking);
	}

}
