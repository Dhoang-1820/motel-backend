package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.TenantDto;
import com.petproject.motelservice.domain.payload.request.ReturnRoomRequest;

public interface TenantsServices {
	
	public List<TenantDto> getTenantByAccomodation(Integer id); 
	
	public TenantDto createOrUpdate(TenantDto request);
	
	public void returnRoom(ReturnRoomRequest request);
}
