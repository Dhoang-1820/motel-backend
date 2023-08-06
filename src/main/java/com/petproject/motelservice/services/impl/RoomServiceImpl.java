package com.petproject.motelservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.RoomDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.services.RoomService;

@Service
public class RoomServiceImpl implements RoomService {
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	AccomodationsRepository accomodationsRepository;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public List<RoomDto> getRoomsByAccomodation(Integer id) {
		Accomodations accomodations = accomodationsRepository.findById(id).orElse(null);
		List<Rooms> rooms = roomRepository.findByAccomodations(accomodations);
		List<RoomDto> result = rooms.stream()
                .map(source -> mapper.map(source, RoomDto.class))
                .collect(Collectors.toList());
		return result;
	}

	@Override
	public RoomDto saveRoom(RoomDto request) {
		Rooms room = mapper.map(request, Rooms.class);
		Accomodations accomodations = accomodationsRepository.findById(request.getAccomodationId()).orElse(null);
		room.setAccomodations(accomodations);
		room = roomRepository.save(room);
		RoomDto result = mapper.map(room, RoomDto.class);
		return result;
	}

	@Override
	public void removeRoom(Integer roomId) {
		Rooms room = roomRepository.findById(roomId).orElse(null);
		if (room != null) {
			roomRepository.delete(room);			
		}
	}
	
	
}
