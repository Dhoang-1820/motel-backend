package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.TenantDto;
import com.petproject.motelservice.domain.inventory.Tenants;

public interface TenantsService {

	List<TenantDto> getTenantByAccomodation(Integer id);

	TenantDto createOrUpdate(TenantDto request);

	Boolean checkDuplicatedIdentifyNum(String identifyNum);
	
	Boolean checkDuplicatedEmail(String email);
	
	Boolean checkDuplicatedPhone(String phone);
	
	List<TenantDto> getTenantNotDeposit(Integer id);

	List<TenantDto> getTenantNotContract(Integer id);
	
	List<Tenants> countTenantByUserId(Integer userId);
	
}
