package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.Booking;

public interface BookingRepositoty extends JpaRepository<Booking, Integer> {

}
