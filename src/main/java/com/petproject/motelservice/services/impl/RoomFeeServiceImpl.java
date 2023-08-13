package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.petproject.motelservice.domain.dto.FileUploadDto;
import com.petproject.motelservice.domain.dto.RoomServiceDto;
import com.petproject.motelservice.domain.inventory.Images;
import com.petproject.motelservice.domain.inventory.OtherFees;
import com.petproject.motelservice.domain.inventory.RoomFeeId;
import com.petproject.motelservice.domain.inventory.RoomFees;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.payload.request.RoomFeeRequest;
import com.petproject.motelservice.domain.query.response.OtherFeesResponse;
import com.petproject.motelservice.domain.query.response.RoomFeeResponse;
import com.petproject.motelservice.domain.query.response.RoomServiceResponse;
import com.petproject.motelservice.repository.ImageRepository;
import com.petproject.motelservice.repository.OtherFeeRepository;
import com.petproject.motelservice.repository.RoomFeeRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.services.FileService;
import com.petproject.motelservice.services.RoomFeeService;

@Service
public class RoomFeeServiceImpl implements RoomFeeService {
	
	@Autowired
	RoomFeeRepository roomFeeRepository;
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	OtherFeeRepository otherFeeRepository;

	
	@Autowired
	ImageRepository imageRepository; 

	@Override
	public List<RoomServiceDto> getRoomFeeByAccomodation(Integer accomodationId) {
		List<RoomServiceDto> result = new ArrayList<>();
		RoomServiceDto roomServiceDto = null;
		List<RoomFeeResponse> roomFees = null; 
		List<RoomServiceResponse> roomServices = roomRepository.findRoomServiceByAccomodation(accomodationId);
		for (RoomServiceResponse room : roomServices) {
			roomServiceDto = new RoomServiceDto();
			roomServiceDto.setRoom(room);
			roomFees = roomFeeRepository.findByRoom(room.getRoomId());
			roomServiceDto.setFees(roomFees);
			result.add(roomServiceDto);
			
		}
		return result;
	}

	@Override
	public List<OtherFeesResponse> getRemainFeesDropDown(Integer roomId, Integer accomodationId) {
		List<OtherFeesResponse> fees = otherFeeRepository.findRemainByRoomAndAccomodation(roomId, accomodationId);
		return fees;
	}

	@Override
	public void saveRoomFee(RoomFeeRequest request) {
		RoomFees fee = new RoomFees();
		RoomFeeId id = new RoomFeeId();
		id.setFee(new OtherFees(request.getFeeId()));
		id.setRoom(new Rooms(request.getRoomId()));
		fee.setId(id);
		fee.setQuantity(request.getQuantity());
		roomFeeRepository.save(fee);
	}

	
}
