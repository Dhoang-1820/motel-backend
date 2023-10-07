package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petproject.motelservice.domain.inventory.Tenants;

public interface TenantRepository extends JpaRepository<Tenants, Integer> {
		
	List<Tenants> findByAccomodationsId(@Param("accomodationsId") Integer accomodationsId);
	
	@Query("FROM Tenants tenant LEFT JOIN Deposits deposit ON tenant.id = deposit.tenant.id WHERE deposit.id IS NULL AND tenant.accomodations.id = :id")
	List<Tenants> findTenantWithoutDeposit(@Param("id") Integer accomodationId);
}
