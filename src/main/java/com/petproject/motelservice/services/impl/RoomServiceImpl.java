package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.RoomDto;
import com.petproject.motelservice.domain.dto.TenantDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Contract;
import com.petproject.motelservice.domain.inventory.Deposits;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.Tenants;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.ContractRepository;
import com.petproject.motelservice.repository.DepositRepository;
import com.petproject.motelservice.repository.ImageRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.services.FileService;
import com.petproject.motelservice.services.RoomService;

@Service
public class RoomServiceImpl implements RoomService {
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	DepositRepository depositRepository;
	
	@Autowired
	AccomodationsRepository accomodationsRepository;
	
	@Autowired
	ContractRepository contractRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	FileService storageService;
	
	@Autowired
	ModelMapper mapper;
	
	private static Log logger = LogFactory.getLog(RoomServiceImpl.class);

	@Override
	public List<RoomDto> getRoomsByAccomodation(Integer id) {
		List<RoomDto> result = new ArrayList<>();
		RoomDto dto = null;
		Accomodations accomodations = accomodationsRepository.findById(id).orElse(null);
		List<Rooms> rooms = roomRepository.findByAccomodationsAndIsActive(accomodations, true);
		Contract contract = null;
		Integer tenantsNum = null;
		for (Rooms room : rooms) {
			dto = new RoomDto();
			contract = contractRepository.findByRoomIdAndIsActive(room.getId(), true);
			if (contract != null) {
				tenantsNum = contract.getTenants().size();				
			} else {
				tenantsNum = 0;
			}
			dto.setId(room.getId());
			dto.setAccomodationId(id);
			dto.setAcreage(room.getAcreage());
			dto.setCapacity(room.getCapacity());
			dto.setCurrentTenantNum(tenantsNum);
			dto.setIsRent(room.getIsRent());
			dto.setName(room.getName());
			dto.setPrice(room.getPrice());
			result.add(dto);
		}
		
		return result;
	}

	@Override
	public List<RoomDto> saveRoom(RoomDto request) {
		Rooms room = mapper.map(request, Rooms.class);
		Accomodations accomodations = accomodationsRepository.findById(request.getAccomodationId()).orElse(null);
		room.setIsActive(Boolean.TRUE);
		room.setAccomodations(accomodations);
		room = roomRepository.save(room);
		return getRoomsByAccomodation(request.getAccomodationId());
	}
	
	@Override
	public Map<String, Object> checkIsRoomHasDeposit(Integer roomId) {
		Map<String, Object> result = new HashMap<>();
		Deposits deposit = depositRepository.findByRoomId(roomId);
		if (deposit != null) {
			result.put("isBooked", Boolean.TRUE);
			Tenants tenant = deposit.getTenant();
			TenantDto dto = new TenantDto();
			dto.setFirstName(tenant.getFirstName());
			dto.setLastName(tenant.getLastName());
			dto.setId(tenant.getId());
			result.put("depositor", dto);
			result.put("depositMoney", deposit.getDeposit());
		} else {
			result.put("isBooked", Boolean.FALSE);
		}
		return result;
	}

	
	@Override
	public Boolean isDuplicateRoom(String roomName) {
		Boolean result = false;
		Rooms room = roomRepository.findByNameAndIsActive(roomName, true);
		if (room != null) {
			result = true;
		}
		return result;
	}

	@Override
	public void removeRoom(Integer roomId) {
		try {
			Rooms room = roomRepository.findById(roomId).orElse(null);
			if (room != null) {
				room.setIsActive(Boolean.FALSE);
				roomRepository.save(room);			
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public List<RoomResponse> getRoomDropDown(Integer accomodationId) {
		Accomodations accomodations = accomodationsRepository.findById(accomodationId).orElse(null);
		List<Rooms> rooms = roomRepository.findByAccomodationsAndIsActive(accomodations, true);
		List<RoomResponse> result = rooms.stream()
                .map(source -> mapper.map(source, RoomResponse.class))
                .collect(Collectors.toList());
		return result;
	}
	

	@Override
	public RoomResponse getRoomById(Integer roomId) {
		Rooms room = roomRepository.findById(roomId).orElse(null);
		return new RoomResponse(room.getId(), room.getName(), room.getPrice(), room.getCapacity());
	}

	@Override
	public List<RoomResponse> getRoomNoDeposit(Integer accomodationId) {
		List<Rooms> rooms = roomRepository.findRoomNoDepostit(accomodationId);
		List<RoomResponse> result = rooms.stream()
                .map(source -> mapper.map(source, RoomResponse.class))
                .collect(Collectors.toList());
		return result;
	}
	
	@Override
	public List<RoomResponse> getRoomNoPost(Integer accomodationId) {
		List<Rooms> rooms = roomRepository.findRoomNoPost(accomodationId);
		List<RoomResponse> result = rooms.stream()
                .map(source -> mapper.map(source, RoomResponse.class))
                .collect(Collectors.toList());
		return result;
	}
	
	
	@Override
	public Map<String, Date> getRoomRentedDate(Integer roomId) {
		 Map<String, Date> result = new HashMap<>();
		 Contract contract = contractRepository.findByRoomIdAndIsActive(roomId, true);
		 result.put("date", contract.getStartDate());
		return result;
	}

	@Override
	public List<RoomResponse> getRoomNoPostAndDeposit(Integer accomodationId) {
		Set<RoomResponse> noDeposit = new HashSet<>();
		noDeposit.addAll(getRoomNoDeposit(accomodationId));
		Set<RoomResponse> noPost = new HashSet<>();
		noPost.addAll(getRoomNoPost(accomodationId));
		noPost.retainAll(noDeposit);
		return new ArrayList<>(noPost);
	}

	@Override
	public List<RoomResponse> getRoomNoRented(Integer accomodationId) {
		List<Rooms> rooms = roomRepository.findRoomNoRented(accomodationId);
		List<RoomResponse> result = new ArrayList<>();
		for (Rooms room : rooms) {
			result.add(convert2RoomReponse(room));
		}
		return result;
	}
	
	private RoomResponse convert2RoomReponse(Rooms room) {
		return new RoomResponse(room.getId(), room.getName(), room.getPrice(), room.getCapacity());
	}
	
	@Override
	public List<RoomResponse> getRoomRented(Integer accomodationId) {
		List<Rooms> rooms = roomRepository.findRoomRented(accomodationId);
		List<RoomResponse> result = rooms.stream()
                .map(source -> mapper.map(source, RoomResponse.class))
                .collect(Collectors.toList());
		return result;
	}

	@Override
	public List<RoomResponse> getRoomNoElectricWaterIndex(Integer accomodationId, Date month) {
		List<Rooms> rooms = roomRepository.findRoomNoElectricWater(accomodationId, month);
		List<RoomResponse> result = rooms.stream()
                .map(source -> mapper.map(source, RoomResponse.class))
                .collect(Collectors.toList());
		return result;
	}
	
}
