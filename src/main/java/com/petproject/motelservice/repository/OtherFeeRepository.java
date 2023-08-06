package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petproject.motelservice.domain.inventory.OtherFees;

public interface OtherFeeRepository extends JpaRepository<OtherFees, Integer> {
	
	@Query("FROM OtherFees fee WHERE fee.accomodations.id = :accomId AND fee.id NOT IN (:listId)")
	List<OtherFees> findByAccomodationIdAndNotIn(@Param("accomId") Integer id, @Param("listId") List<Integer> existsId);
}
