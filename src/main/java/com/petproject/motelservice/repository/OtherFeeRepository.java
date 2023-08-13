package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petproject.motelservice.domain.inventory.OtherFees;
import com.petproject.motelservice.domain.query.response.OtherFeesResponse;

public interface OtherFeeRepository extends JpaRepository<OtherFees, Integer> {
	
	@Query("FROM OtherFees fee WHERE fee.accomodations.id = :accomId AND fee.id NOT IN (:listId)")
	List<OtherFees> findByAccomodationIdAndNotIn(@Param("accomId") Integer id, @Param("listId") List<Integer> existsId);
	
	@Query(nativeQuery = true, value = "select fee.* from other_fees fee left join (select * from room_fees where room_id = :roomId) room on fee.id = room.fee_id where accomodation_id = :accmodationId and room.fee_id is null")
	List<OtherFeesResponse> findRemainByRoomAndAccomodation(@Param("roomId") Integer roomId, @Param("accmodationId") Integer accmodationId);
}
