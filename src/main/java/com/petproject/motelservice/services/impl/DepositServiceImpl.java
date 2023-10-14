package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.DepositDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Deposits;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.Tenants;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.domain.query.response.DepositResponse;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.DepositRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.repository.TenantRepository;
import com.petproject.motelservice.services.DepositService;


@Service
public class DepositServiceImpl implements DepositService {
	
	@Autowired
	DepositRepository depositRepository;
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	TenantRepository tenantRepository;
	
	@Autowired
	AccomodationsRepository accomodationsRepository;

	@Override
	public List<DepositDto> getDepositByAccomodation(Integer accomodationId) {
		List<DepositDto> result = new ArrayList<>();
		List<DepositResponse> deposits = depositRepository.findByAccomodation(accomodationId);
		DepositDto dto = null;
		for (DepositResponse item : deposits) {
			dto = new DepositDto();
			dto.setId(item.getId());
			dto.setDeposit(item.getDeposit());
			dto.setDueDate(item.getDueDate());
			dto.setIsActive(item.getIsActive());
			dto.setIsRepaid(item.getIsRepaid());
			dto.setNote(item.getNote());
			dto.setRoom(new RoomResponse(item.getRoomId(), item.getRoomName(), item.getRoomPrice()));
			dto.setStartDate(item.getStartDate());
			dto.setTenantId(item.getTenantId());
			dto.setEmail(item.getEmail());
			dto.setFirstName(item.getFirstName());
			dto.setLastName(item.getLastName());
			dto.setPhone(item.getPhone());
			dto.setIdentifyNum(item.getIdentifyNum());
			result.add(dto);
		}
		return result;
	}

	@Override
	public List<DepositDto> saveDeposit(DepositDto request) {
		Deposits deposit = null;
		if (request.getId() != null) {
			deposit = depositRepository.findById(request.getId()).orElse(null);
		} else {
			deposit = new Deposits();
			deposit.setCreatedAt(new Date());
		}
		deposit.setDeposit(request.getDeposit());
		deposit.setDueDate(request.getDueDate());
		deposit.setNote(request.getNote());
		deposit.setStartDate(request.getStartDate());
		Rooms room = roomRepository.findById(request.getRoom().getId()).orElse(null);
		deposit.setRoom(room);
		Tenants tenant = null;
		if (request.getTenantId() != null) {
			tenant = tenantRepository.findById(request.getTenantId()).orElse(null);
		} else {
			tenant = new Tenants();
		}
		Accomodations accomodation = accomodationsRepository.findById(request.getAccomodationId()).orElse(null);
		tenant.setAccomodations(accomodation);
		tenant.setFirstName(request.getFirstName());
		tenant.setLastName(request.getLastName());
		tenant.setPhone(request.getPhone());
		tenant.setEmail(request.getEmail());
		tenant.setIdentifyNum(request.getIdentifyNum());
		tenant = tenantRepository.save(tenant);
		deposit.setTenant(tenant);
		depositRepository.save(deposit);
		return getDepositByAccomodation(request.getAccomodationId());
	}
	
	
}
