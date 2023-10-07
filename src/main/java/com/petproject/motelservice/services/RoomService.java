package com.petproject.motelservice.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.domain.dto.RoomDto;
import com.petproject.motelservice.domain.dto.RoomImageDto;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.domain.query.response.RoomServiceResponse;

public interface RoomService {
	
	public List<RoomDto> getRoomsByAccomodation(Integer id);
	
	public List<RoomDto> saveRoom(RoomDto room);
	
	public void removeRoom(Integer roomId);
	
	public List<RoomResponse> getRoomDropDown(Integer accomodationId);
	
	public List<RoomServiceResponse> getRoomNotHasService(Integer accomodationId);

	public RoomImageDto getRoomImages(Integer roomId);
	
	public void removeImage(Integer imageId);

	void saveRoomImage(MultipartFile[] images, Integer roomId);

	void changeRoomImage(MultipartFile[] images, Integer imageId);

	List<RoomResponse> getRoomNoDeposit(Integer accomodationId);
}
