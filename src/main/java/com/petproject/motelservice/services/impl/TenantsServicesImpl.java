package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.TenantDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.Tenants;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.repository.AccomodationsRepository;
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

	@Override
	public List<TenantDto> getTenantByAccomodation(Integer id) {
		Accomodations accomodations = accomodationsRepository.findById(id).orElse(null);
		List<Rooms> rooms = roomRepository.findByAccomodations(accomodations);
		List<TenantDto> result = new ArrayList<>();
		TenantDto tenantDto = null;
		List<Tenants> tenants = null;
		for (Rooms room : rooms) {
			tenants = room.getTenants();
			for (Tenants tenant : tenants) {
				tenantDto = convertToDto(tenant, room);
				result.add(tenantDto);
			}
			
		}
		return result;
	}
	
	private TenantDto convertToDto(Tenants tenant, Rooms room) {
		TenantDto tenantDto = new TenantDto();
		tenantDto.setId(tenant.getId());
		tenantDto.setFirstName(tenant.getFirstName());
		tenantDto.setLastName(tenant.getLastName());
		tenantDto.setIdentifyNum(tenant.getIdentifyNum());
		tenantDto.setIsStayed(tenant.getIsStayed());
		tenantDto.setStartDate(tenant.getStartDate());
		tenantDto.setPhone(tenant.getPhone());
		tenantDto.setEmail(tenant.getEmail());
		tenantDto.setRoom(new RoomResponse(room.getId(), room.getName()));
		return tenantDto;
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
		Rooms room = roomRepository.findById(request.getRoom().getId()).orElse(null);
		tenant.setRoom(room);
		tenant = tenantRepository.save(tenant);
		return convertToDto(tenant, room);
	}

	
}
