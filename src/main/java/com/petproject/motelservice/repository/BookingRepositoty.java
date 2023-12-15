package com.petproject.motelservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petproject.motelservice.domain.inventory.Booking;

public interface BookingRepositoty extends JpaRepository<Booking, Integer> {
	
	@Query("SELECT booking FROM Booking booking WHERE booking.isActive = true AND booking.post.user.id = :userId")
	List<Booking> findBookingByUserId(Integer userId);
	
	@Query("SELECT booking FROM Booking booking  WHERE day(booking.bookingDate) = day(:date) AND month(booking.bookingDate) = month(:date) AND year(booking.bookingDate) = year(:date) AND booking.isActive = true AND booking.post.user.id = :userId")
	List<Booking> findBookingByUserIdAndDate(Integer userId, Date date);
}
