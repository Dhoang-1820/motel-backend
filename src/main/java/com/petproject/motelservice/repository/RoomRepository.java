package com.petproject.motelservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.query.response.RoomBillEmail;
import com.petproject.motelservice.domain.query.response.RoomBillResponse;
import com.petproject.motelservice.domain.query.response.RoomServiceResponse;

public interface RoomRepository extends JpaRepository<Rooms, Integer> {
	
	List<Rooms> findByAccomodations(Accomodations accomodations);
	
	@Query(nativeQuery = true, value = "select room.id as roomId, room.name as room, count(tenant.id) as numPerson, tenant.start_date as startDate from tenants tenant join ( select room.id, room.name, room.accomodation_id from rooms room join (select * from room_fees group by room_id) fee on room.id = fee.room_id where room.accomodation_id = :accomodationId ) room on room.id = tenant.room_id where tenant.is_stayed = 1 group by tenant.room_id")
	List<RoomServiceResponse> findRoomServiceByAccomodation(@Param("accomodationId") Integer accomodationId);
	
	Rooms findByName(String name);
	
	@Query(nativeQuery = true, value = "select room.id as roomId, room.name as room, count(tenant.id) as numPerson, tenant.start_date as startDate from tenants tenant join ( select room.id, room.name, room.accomodation_id from rooms room left join (select * from room_fees group by room_id) fee on room.id = fee.room_id where room.accomodation_id = :accomodationId and fee.room_id is null ) room on room.id = tenant.room_id where tenant.is_stayed = 1 group by tenant.room_id")
	List<RoomServiceResponse> findRoomNotHasService(@Param("accomodationId") Integer accomodationId);
	
	@Query(nativeQuery = true, value = "select rooms.id, rooms.name as name, bills.id as billId, bills.is_sent as isSent, bills.electric_num as electricNum, bills.water_num as waterNum, bills.date as month, bills.total_price as totalPrice, bills.is_pay as isPay, bills.created_at as createAt from rooms join bills on rooms.id = bills.room_id where rooms.accomodation_id = :accomodationId and month(bills.date) = month(:monthBill) and year(bills.date) = year(:monthBill) group by rooms.id order by rooms.id")
	List<RoomBillResponse> findRoomHasBill(@Param("accomodationId") Integer accomodationId, @Param("monthBill") Date month);
	
	@Query(nativeQuery = true, value = "select accomodations.electric_price as electricPrice, accomodations.water_price as waterPrice, room.*, (accomodations.electric_price * room.electricNum) as totalElectric,  (accomodations.water_price * room.waterNum) as totalWater from accomodations join ( select rooms.id, rooms.accomodation_id, rooms.name as name, bills.is_sent as isSent, bills.electric_num as electricNum, bills.water_num as waterNum, bills.date as month, bills.total_price as totalPrice, bills.is_pay as isPay, bills.created_at as createAt from rooms join bills on rooms.id = bills.room_id where rooms.id = :roomId and month(bills.date) = month(:month) and year(bills.date) = year(:month) group by rooms.id order by rooms.id) room on accomodations.id = room.accomodation_id")
	RoomBillEmail findRoomBillByMonth(@Param("roomId") Integer roomId, @Param("month") Date month);
	
	@Query("SELECT rooms FROM Rooms rooms LEFT JOIN Contract contract ON rooms.id = contract.room.id WHERE rooms.accomodations.id = :accomodationId AND contract IS NULL AND rooms.id NOT IN (SELECT deposit.room.id FROM Deposits deposit WHERE deposit.isActive = true)")
	List<Rooms> findRoomNoDepostit(@Param("accomodationId") Integer accomodationId); 
	
	@Query("FROM Rooms room WHERE room.isRent = false AND room.accomodations.id = :accomodationId")
	List<Rooms> findRoomNoRented(@Param("accomodationId") Integer accomodationId); 
	
	@Query("FROM Rooms room WHERE room.isRent = true AND room.accomodations.id = :accomodationId")
	List<Rooms> findRoomRented(@Param("accomodationId") Integer accomodationId); 
	
	@Query("SELECT rooms FROM Rooms rooms LEFT JOIN Post post ON rooms.id = post.room.id WHERE rooms.accomodations.id = :accomodationId AND post IS NULL")
	List<Rooms> findRoomNoPost(@Param("accomodationId") Integer accomodationId); 
	
	@Query("FROM Rooms room WHERE room.accomodations.id = :accomodationId AND room.id NOT IN (SELECT num.room.id FROM ElectricWaterNum num WHERE month(num.month) = month(:month) AND year(num.month) = year(:month)) AND room.id NOT IN (SELECT bill.room.id FROM Bills bill WHERE bill.isActive = true AND month(bill.billDate) = month(:month) AND year(bill.billDate) = year(:month))")
	List<Rooms> findRoomNoElectricWater(@Param("accomodationId") Integer accomodationId, Date month);
	
}
