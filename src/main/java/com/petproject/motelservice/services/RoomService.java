package com.petproject.motelservice.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.domain.dto.RoomDto;
import com.petproject.motelservice.domain.dto.RoomImageDto;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.domain.query.response.RoomServiceResponse;

public interface RoomService {
	
	List<RoomDto> getRoomsByAccomodation(Integer id);
	
	List<RoomDto> saveRoom(RoomDto room);
	
	void removeRoom(Integer roomId);
	
	List<RoomResponse> getRoomDropDown(Integer accomodationId);
	
	RoomResponse getRoomById(Integer roomId);
	
	List<RoomServiceResponse> getRoomNotHasService(Integer accomodationId);

	RoomImageDto getRoomImages(Integer roomId);
	
	void removeImage(Integer imageId);

	void saveRoomImage(MultipartFile[] images, Integer roomId);

	void changeRoomImage(MultipartFile[] images, Integer imageId);

	List<RoomResponse> getRoomNoDeposit(Integer accomodationId);
	
	List<RoomResponse> getRoomRented(Integer accomodationId);

	List<RoomResponse> getRoomNoRented(Integer accomodationId);

	List<RoomResponse> getRoomNoPost(Integer accomodationId);
	
	List<RoomResponse> getRoomNoPostAndDeposit(Integer accomodationId);
	
	List<RoomResponse> getRoomNoElectricWaterIndex(Integer accomodationId, Date month);

	Map<String, Object> checkIsRoomHasDeposit(Integer roomId);
}
