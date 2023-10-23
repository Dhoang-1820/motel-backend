package com.petproject.motelservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petproject.motelservice.domain.inventory.ElectricWaterNum;

public interface ElectricWaterNumRepository extends JpaRepository<ElectricWaterNum, Integer>{

	@Query("FROM ElectricWaterNum num WHERE num.room.accomodations.id = :accomodationId AND month(num.month) = month(:month) AND year(num.month) = year(:month)")
	List<ElectricWaterNum> findByAccomodationAndMonth(Integer accomodationId, Date month);
	
	@Query("FROM ElectricWaterNum num WHERE num.room.id = :roomId AND month(num.month) = month(:month) AND year(num.month) = year(:month)")
	ElectricWaterNum findByRoomIdAndMonth(Integer roomId, Date month);
}
