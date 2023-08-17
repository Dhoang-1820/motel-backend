package com.petproject.motelservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petproject.motelservice.domain.inventory.RoomFeeId;
import com.petproject.motelservice.domain.inventory.RoomFees;
import com.petproject.motelservice.domain.query.response.RoomFeeResponse;

public interface RoomFeeRepository extends JpaRepository<RoomFees, RoomFeeId> {
	
	@Query(nativeQuery = true, value = "SELECT fee.fee_id as feeId, fee.quantity, fee.room_id as roomId, otherFee.name as feeName, otherFee.unit, otherFee.price FROM room_fees fee JOIN other_fees otherFee on fee.fee_id = otherFee.id WHERE fee.room_id = :roomId")
	public List<RoomFeeResponse> findByRoom(@Param("roomId") Integer roomId);
	
	@Query(nativeQuery = true, value = "SELECT fee.fee_id as feeId, fee.quantity, room.room_id as roomId, room.name as roomName, otherFee.name as feeName, otherFee.unit, otherFee.price, room.total_price FROM room_fees fee JOIN other_fees otherFee on fee.fee_id = otherFee.id JOIN (select rooms.*, bills.id as billId, bills.total_price, bills.date, bills.room_id from rooms join bills on rooms.id = bills.room_id where month(bills.date) = month(:monthBill) and year(bills.date) = year(:monthBill) and rooms.accomodation_id = :accomodationId) room ON room.id = fee.room_id order by room.room_id")
	List<RoomFeeResponse> findRoomMonthBillByAccomodation(@Param("accomodationId") Integer accomodationId, @Param("monthBill") Date month);

	@Query(nativeQuery = true, value = "SELECT fee.fee_id as feeId, fee.quantity, room.room_id as roomId, room.name as roomName, otherFee.name as feeName, otherFee.unit, otherFee.price, (fee.quantity * otherFee.price) as total, room.total_price FROM room_fees fee JOIN other_fees otherFee on fee.fee_id = otherFee.id JOIN (select rooms.*, bills.id as billId, bills.total_price, bills.date, bills.room_id from rooms join bills on rooms.id = bills.room_id where month(bills.date) = month(:month) and year(bills.date) = year(:month) and rooms.id = :roomId) room ON room.id = fee.room_id order by room.room_id")
	List<RoomFeeResponse> findRoomMonthBill(@Param("roomId") Integer roomId, @Param("month") Date month);
	
	@Query("FROM RoomFees fees WHERE fees.id.room.id = :roomId and fees.id.fee.id = :feeId")
	RoomFees findById(@Param("roomId") Integer roomId, @Param("feeId") Integer feeId);
}
