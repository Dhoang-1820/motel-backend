package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.TenantDto;
import com.petproject.motelservice.domain.payload.request.ReturnRoomRequest;

public interface TenantsServices {

	List<TenantDto> getTenantByAccomodation(Integer id);

	TenantDto createOrUpdate(TenantDto request);

	void returnRoom(ReturnRoomRequest request);
	
	List<TenantDto> getTenantNotDeposit(Integer id);
	
}
