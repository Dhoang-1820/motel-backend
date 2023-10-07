package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.TenantDto;
import com.petproject.motelservice.domain.inventory.Tenants;
import com.petproject.motelservice.domain.payload.request.ReturnRoomRequest;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.DepositRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.repository.TenantRepository;
import com.petproject.motelservice.services.TenantsServices;

@Service
public class TenantsServicesImpl implements TenantsServices {
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	AccomodationsRepository accomodationsRepository;
	
	@Autowired
	TenantRepository tenantRepository;
	
	@Autowired
	DepositRepository depositRepository;

	@Override
	public List<TenantDto> getTenantByAccomodation(Integer id) {
		List<Tenants> tenants = tenantRepository.findByAccomodationsId(id);
		List<TenantDto> result = new ArrayList<>();
		for (Tenants tenant : tenants) {
			result.add(convert2Dto(tenant));
		}
		return result;
	}
	


	@Override
	public TenantDto createOrUpdate(TenantDto request) {
		Tenants tenant = null;
		if (request.getId() != null) {
			tenant = tenantRepository.findById(request.getId()).orElse(null);
		} else {
			tenant = new Tenants();
		}
		tenant.setFirstName(request.getFirstName());
		tenant.setLastName(request.getLastName());
		tenant.setIdentifyNum(request.getIdentifyNum());
		tenant.setImageUrl(request.getImageUrl());
		tenant.setIsStayed(request.getIsStayed());
		tenant.setStartDate(request.getStartDate());
		tenant.setPhone(request.getPhone());
		tenant.setEmail(request.getEmail());
//		Rooms room = roomRepository.findById(request.getRoom().getId()).orElse(null);
//		tenant.setRoom(room);
		tenant = tenantRepository.save(tenant);
//		if (!room.getIsRent()) {
//			room.setIsRent(true);
//			roomRepository.save(room);
//		}
		return null;
	}

	@Override
	public void returnRoom(ReturnRoomRequest request) {
		Tenants tenant = tenantRepository.findById(request.getId()).orElse(null);
		tenant.setIsStayed(false);
		tenant.setEndDate(request.getReturnDate());
		tenant = tenantRepository.save(tenant);
//		Rooms room = tenant.getRoom();
//		List<Tenants> tenants = tenantRepository.findByRoomAndIsStayed(room, true);
//		if (tenants.isEmpty()) {
//			room.setIsRent(false);
//			roomRepository.save(room);
//		}
	}

	private TenantDto convert2Dto(Tenants tenant) {
		TenantDto tenantDto = new TenantDto();
		tenantDto.setId(tenant.getId());
		tenantDto.setEmail(tenant.getEmail());
		tenantDto.setFirstName(tenant.getFirstName());
		tenantDto.setLastName(tenant.getLastName());
		tenantDto.setIdentifyNum(tenant.getIdentifyNum());
		tenantDto.setPhone(tenant.getPhone());
		tenantDto.setStartDate(tenant.getStartDate());
		tenantDto.setIsStayed(tenant.getIsStayed());
		return tenantDto;
	}

	@Override
	public List<TenantDto> getTenantNotDeposit(Integer id) {
		List<Tenants> tenants = tenantRepository.findTenantWithoutDeposit(id);
		List<TenantDto> result = new ArrayList<>();
		for (Tenants tenant : tenants) {
			result.add(convert2Dto(tenant));
		}
		return result;
	}

}
