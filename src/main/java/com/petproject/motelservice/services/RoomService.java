package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.RoomDto;

public interface RoomService {
	
	public List<RoomDto> getRoomsByAccomodation(Integer id);
	
	public RoomDto saveRoom(RoomDto room);
	
	public void removeRoom(Integer roomId);
}
