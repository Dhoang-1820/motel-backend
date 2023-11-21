package com.petproject.motelservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Rooms;

public interface RoomRepository extends JpaRepository<Rooms, Integer> {
	
	List<Rooms> findByAccomodationsAndIsActive(Accomodations accomodations, Boolean isActive);
	
	Rooms findByNameAndIsActive(String name, Boolean isActive);
	
	@Query("SELECT rooms FROM Rooms rooms  WHERE rooms.accomodations.id = :accomodationId AND rooms NOT IN (SELECT contract.room FROM Contract contract WHERE contract.isActive = true) AND rooms.isActive = true AND rooms.id NOT IN (SELECT deposit.room.id FROM Deposits deposit WHERE deposit.isActive = true)")
	List<Rooms> findRoomNoDepostit(@Param("accomodationId") Integer accomodationId); 
	
	@Query("FROM Rooms room WHERE room.isRent = false AND room.isActive = true AND room.accomodations.id = :accomodationId")
	List<Rooms> findRoomNoRented(@Param("accomodationId") Integer accomodationId); 
	
	@Query("FROM Rooms room WHERE room.isRent = true AND room.isActive = true AND room.accomodations.id = :accomodationId")
	List<Rooms> findRoomRented(@Param("accomodationId") Integer accomodationId); 
	
	@Query("SELECT rooms FROM Rooms rooms LEFT JOIN Post post ON rooms.id = post.room.id WHERE rooms.accomodations.id = :accomodationId AND rooms.isActive = true AND post IS NULL")
	List<Rooms> findRoomNoPost(@Param("accomodationId") Integer accomodationId); 
	
	@Query("FROM Rooms room WHERE room.accomodations.id = :accomodationId AND room.isActive = true AND room.id NOT IN (SELECT num.room.id FROM ElectricWaterNum num WHERE month(num.month) = month(:month) AND year(num.month) = year(:month)) AND room.id NOT IN (SELECT bill.room.id FROM Bills bill WHERE bill.isActive = true AND month(bill.billDate) = month(:month) AND year(bill.billDate) = year(:month))")
	List<Rooms> findRoomNoElectricWater(@Param("accomodationId") Integer accomodationId, Date month);
	
}
