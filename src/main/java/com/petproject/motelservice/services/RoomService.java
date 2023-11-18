package com.petproject.motelservice.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.petproject.motelservice.domain.dto.RoomDto;
import com.petproject.motelservice.domain.payload.response.RoomResponse;

public interface RoomService {
	
	List<RoomDto> getRoomsByAccomodation(Integer id);
	
	List<RoomDto> saveRoom(RoomDto room);
	
	void removeRoom(Integer roomId);
	
	Boolean isDuplicateRoom(String roomName);
	
	List<RoomResponse> getRoomDropDown(Integer accomodationId);
	
	RoomResponse getRoomById(Integer roomId);
	
	List<RoomResponse> getRoomNoDeposit(Integer accomodationId);
	
	List<RoomResponse> getRoomRented(Integer accomodationId);
	
	Map<String, Date> getRoomRentedDate(Integer accomodationId);

	List<RoomResponse> getRoomNoRented(Integer accomodationId);

	List<RoomResponse> getRoomNoPost(Integer accomodationId);
	
	List<RoomResponse> getRoomNoPostAndDeposit(Integer accomodationId);
	
	List<RoomResponse> getRoomNoElectricWaterIndex(Integer accomodationId, Date month);

	Map<String, Object> checkIsRoomHasDeposit(Integer roomId);
}
