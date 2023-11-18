package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petproject.motelservice.domain.inventory.Accomodations;

public interface AccomodationsRepository extends JpaRepository<Accomodations, Integer> {
	
	List<Accomodations> findByUserIdAndIsActive(Integer userId, Boolean isActive);
	
	@Query("SELECT accomodation FROM Accomodations accomodation INNER JOIN Rooms room ON accomodation.id = room.accomodations.id WHERE room.id IN (SELECT post.room.id FROM Post post)")
	List<Accomodations> findAllByPost();
}
