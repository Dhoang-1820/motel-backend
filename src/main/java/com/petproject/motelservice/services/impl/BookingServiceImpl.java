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
import com.petproject.motelservice.domain.inventory.Address;
import com.petproject.motelservice.domain.inventory.Booking;
import com.petproject.motelservice.domain.inventory.District;
import com.petproject.motelservice.domain.inventory.EPostStatus;
import com.petproject.motelservice.domain.inventory.Post;
import com.petproject.motelservice.domain.inventory.PostStatus;
import com.petproject.motelservice.domain.inventory.Province;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.inventory.Ward;
import com.petproject.motelservice.domain.payload.Email;
import com.petproject.motelservice.repository.BookingRepositoty;
import com.petproject.motelservice.repository.PostRepository;
import com.petproject.motelservice.repository.PostStatusRepository;
import com.petproject.motelservice.repository.UsersRepository;
import com.petproject.motelservice.services.BookingService;
import com.petproject.motelservice.services.MailService;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	BookingRepositoty bookingRepository;

	@Autowired
	PostRepository postRepository;
	
	@Autowired
	PostStatusRepository postStatusRepository;

	@Autowired
	MailService mailService;

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	ModelMapper mapper;

	@Override
	public BookingDto saveBooking(BookingDto request) {
		Booking booking = new Booking();
		booking.setEmail(request.getEmail());
		booking.setName(request.getName());
		booking.setPhone(request.getPhone());
		booking.setReviewDate(request.getReviewDate());
		booking.setBookingDate(new Date());
		booking.setIsActive(Boolean.TRUE);
		Post post = postRepository.findById(request.getPostId()).orElse(null);
		int roomEmpty = post.getEmptyRoomNum();
		roomEmpty -= 1;
		post.setEmptyRoomNum(roomEmpty);
		if (roomEmpty == 0) {
			PostStatus status = postStatusRepository.findByName(EPostStatus.REMOVED);
			post.setPostStatus(status);
		}
		postRepository.save(post);
		booking.setPost(post);
		booking = bookingRepository.save(booking);
		BookingDto result = convert2Dto(booking);
		Users user = usersRepository.findByUserId(request.getUserId());
		sendOutNotification(booking, user, post);
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
		Post post = booking.getPost();
		Address address = post.getAddress();
		Ward ward = address.getWard();
		District district = ward.getDistrict();
		Province province = district.getProvince();
		dto.setAccomodation(address.getAddressLine() + ", " + ward.getWard() + ", " + district.getDistrict() + ", "
				+ province.getProvince());
		dto.setId(booking.getId());
		dto.setCreatedDate(booking.getBookingDate());
		dto.setEmail(booking.getEmail());
		dto.setName(booking.getName());
		dto.setPhone(booking.getPhone());
		dto.setReviewDate(booking.getBookingDate());
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

	private void sendBookingNotification(Booking booking, Users user, Post post) {
		Email email = new Email();

		try {
			Map<String, Object> properties = new HashMap<>();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
			String bookingDate = dateFormat.format(booking.getBookingDate());
			String reviewDate = null;
			if (booking.getReviewDate() != null) {
				reviewDate = dateFormat.format(booking.getReviewDate());
			}

			email.setSubject("Motel service: Thông báo đặt phòng");
			Address address = post.getAddress();
			Ward ward = address.getWard();
			District district = ward.getDistrict();
			Province province = district.getProvince();
			properties.put("accomodationName", address.getAddressLine() + ", " + ward.getWard() + ", "
					+ district.getDistrict() + ", " + province.getProvince());
			properties.put("lanlord", user.getFirstname() + " " + user.getLastname());
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

	public void sendOutNotification(Booking request, Users users, Post post) {
		sendBookingNotification(request, users, post);
	}

	@Override
	public void deactivateBooking(Integer bookingId) {
		Booking booking = bookingRepository.findById(bookingId).orElse(null);
		booking.setIsActive(Boolean.FALSE);
		bookingRepository.save(booking);
	}

	@Override
	public void cancelBooking(Integer bookingId) {
		Booking booking = bookingRepository.findById(bookingId).orElse(null);
		Post post = booking.getPost();
		int roomEmpty = post.getEmptyRoomNum();
		post.setEmptyRoomNum(roomEmpty + 1);
		postRepository.save(post);
		booking.setIsActive(Boolean.FALSE);
		bookingRepository.save(booking);
	}
	
	

}
