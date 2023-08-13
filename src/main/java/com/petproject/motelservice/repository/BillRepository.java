package com.petproject.motelservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petproject.motelservice.domain.inventory.Bills;

public interface BillRepository extends JpaRepository<Bills, Integer> {
	
	@Query("FROM Bills bill WHERE month(bill.billDate) = month(:month) and year(bill.billDate) = year(:month) and bill.room.id = :roomId")
	List<Bills> findBillMonthByRomm(@Param("month") Date month, @Param("roomId") Integer roomId);
	
}
