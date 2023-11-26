package com.petproject.motelservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.petproject.motelservice.domain.inventory.Tenants;

public interface TenantRepository extends JpaRepository<Tenants, Integer> {
		
	List<Tenants> findByAccomodationsId(@Param("accomodationsId") Integer accomodationsId);
	
	@Query("FROM Tenants tenant LEFT JOIN Deposits deposit ON tenant.id = deposit.tenant.id WHERE deposit.id IS NULL AND tenant.contract IS NULL AND tenant.accomodations.id = :id")
	List<Tenants> findTenantWithoutDeposit(@Param("id") Integer accomodationId);
	
	@Query("FROM Tenants tenant WHERE tenant.contract IS NULL")
	List<Tenants> findTenantNotContracted(@Param("id") Integer accomodationId);
	
	@Query("FROM Tenants tenant WHERE tenant.identifyNum = :identifyNum AND tenant.endDate IS NULL")
	Tenants findByIdentifyNum(String identifyNum);
	
	@Query("FROM Tenants tenant WHERE tenant.email = :email AND tenant.endDate IS NULL")
	Tenants findByEmail(String email);
	
	@Query("FROM Tenants tenant WHERE tenant.phone = :phone AND tenant.endDate IS NULL")
	Tenants findByPhone(String phone);
	
	@Modifying
	@Transactional
	@Query("UPDATE Tenants tenant SET tenant.contract = NULL WHERE tenant.contract.id = :contractId")
	int updateTenantContractStatus(Integer contractId);
	
	@Query(nativeQuery = true, value = "select tenants.* from accomodations join rooms on accomodations.id = rooms.accomodation_id join (select room_id, id from contract where contract.is_active = 1) contract on rooms.id = contract.room_id join (select * from tenants where tenants.is_stayed != 0) tenants on contract.id = tenants.contract_id where accomodations.user_id = :userId")
	List<Tenants> countTenantByUserId(@Param("userId") Integer userId);
	
}
