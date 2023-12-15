package com.petproject.motelservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petproject.motelservice.domain.inventory.Deposits;
import com.petproject.motelservice.domain.query.response.DepositResponse;

public interface DepositRepository extends JpaRepository<Deposits, Integer>{

	@Query(value = "SELECT deposit.id, deposit.due_date as dueDate, deposit.is_active as isActive, deposit.start_date as startDate, deposit.deposit as deposit, deposit.created_at as createdAt, deposit.note, deposit.is_repaid as isRepaid, tenant.id as tenantId, tenant.first_name firstName, tenant.last_name as lastName, tenant.phone, tenant.identify_num as identifyNum, tenant.email, room.id as roomId, room.name as roomName, room.accomodation_id as accomodationId, room.price as roomPrice, room.max_capacity as roomCapacity FROM motel_service.deposit join (select id, name, accomodation_id, price, max_capacity from rooms where accomodation_id = :id) room on deposit.room_id = room.id join tenants tenant on deposit.tenant_id = tenant.id where deposit.is_active = 1 and tenant.is_active = 1", nativeQuery = true)
	List<DepositResponse> findByAccomodation(@Param("id") Integer accomodationId);
	
	Deposits findByTenantIdAndIsActive(Integer tenantId, Boolean isActive);
	
	@Query("FROM Deposits deposit WHERE deposit.room.id = :roomId AND deposit.isActive = true")
	Deposits findByRoomId(Integer roomId);
	
	@Query("FROM Deposits deposit INNER JOIN Rooms room ON deposit.room.id = room.id WHERE room.accomodations.id = :accomodationId AND deposit.isRepaid = true AND deposit.isActive = true AND YEAR(deposit.lastChange) = YEAR(:year) GROUP BY MONTH(deposit.lastChange) ORDER BY MONTH(deposit.lastChange)")
	List<Deposits> findRevenueByUserIdAndYear(Integer accomodationId, Date year);
	
}
