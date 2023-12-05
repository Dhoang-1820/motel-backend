package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.petproject.motelservice.domain.inventory.Tenants;

public interface TenantRepository extends JpaRepository<Tenants, Integer> {
		
	List<Tenants> findByAccomodationsIdAndIsActive(@Param("accomodationsId") Integer accomodationsId, Boolean isActive);
	
	@Query("FROM Tenants tenant WHERE tenant.id NOT IN (SELECT deposit.tenant.id FROM Deposits deposit WHERE deposit.isActive = true) AND tenant.contract IS NULL AND tenant.isActive = true AND tenant.accomodations.id = :id")
	List<Tenants> findTenantWithoutDeposit(@Param("id") Integer accomodationId);
	
	@Query("FROM Tenants tenant WHERE tenant.contract IS NULL AND tenant.isActive = true AND tenant.accomodations.id = :accomodationId")
	List<Tenants> findTenantNotContracted(@Param("accomodationId") Integer accomodationId);
	
	@Query("FROM Tenants tenant WHERE tenant.identifyNum = :identifyNum AND tenant.isActive = true AND tenant.endDate IS NULL")
	Tenants findByIdentifyNum(String identifyNum);
	
	@Query("FROM Tenants tenant WHERE tenant.email = :email AND tenant.isActive = true AND tenant.endDate IS NULL")
	Tenants findByEmail(String email);
	
	@Query("FROM Tenants tenant WHERE tenant.phone = :phone AND tenant.isActive = true AND tenant.endDate IS NULL")
	Tenants findByPhone(String phone);
	
	@Modifying
	@Transactional
	@Query("UPDATE Tenants tenant SET tenant.contract = NULL, tenant.isStayed = false, tenant.startDate = NULL WHERE tenant.contract.id = :contractId")
	int updateTenantContractStatus(Integer contractId);
	
	@Query(nativeQuery = true, value = "select tenants.* from accomodations join rooms on accomodations.id = rooms.accomodation_id join (select room_id, id from contract where contract.is_active = 1) contract on rooms.id = contract.room_id join (select * from tenants where tenants.is_stayed != 0 and tenants.is_active = 1) tenants on contract.id = tenants.contract_id where accomodations.user_id = :userId")
	List<Tenants> countTenantByUserId(@Param("userId") Integer userId);
	
}
