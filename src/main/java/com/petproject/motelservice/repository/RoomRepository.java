package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Rooms;

public interface RoomRepository extends JpaRepository<Rooms, Integer> {
	
	List<Rooms> findByAccomodations(Accomodations accomodations);
}
