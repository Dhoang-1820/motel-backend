package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.DepositDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Deposits;
import com.petproject.motelservice.domain.inventory.Post;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.Tenants;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.domain.query.response.DepositResponse;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.DepositRepository;
import com.petproject.motelservice.repository.PostRepository;
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
	
	@Autowired
	PostRepository postRepository;

	@Override
	public List<DepositDto> getDepositByAccomodation(Integer accomodationId) {
		List<DepositDto> result = new ArrayList<>();
		List<DepositResponse> deposits = depositRepository.findByAccomodation(accomodationId);
		for (DepositResponse item : deposits) {
			result.add(convert2Dto(item));
		}
		return result;
	}
	
	private DepositDto convert2Dto(DepositResponse deposit) {
		DepositDto dto = new DepositDto();
		dto.setId(deposit.getId());
		dto.setDeposit(deposit.getDeposit());
		dto.setDueDate(deposit.getDueDate());
		dto.setIsActive(deposit.getIsActive());
		dto.setIsRepaid(deposit.getIsRepaid());
		dto.setNote(deposit.getNote());
		dto.setRoom(new RoomResponse(deposit.getRoomId(), deposit.getRoomName(), deposit.getRoomPrice()));
		dto.setStartDate(deposit.getStartDate());
		dto.setTenantId(deposit.getTenantId());
		dto.setEmail(deposit.getEmail());
		dto.setFirstName(deposit.getFirstName());
		dto.setLastName(deposit.getLastName());
		dto.setPhone(deposit.getPhone());
		dto.setIdentifyNum(deposit.getIdentifyNum());
		return dto;
	}

	@Override
	public Boolean saveDeposit(DepositDto request) {
		Boolean result = false;
		Deposits deposit = null;
		try {
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
			List<Post> posts = postRepository.findByRoomIdAndIsActive(room.getId(), true);
			for (Post post : posts) {
				post.setIsActive(false);
				postRepository.save(post);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
