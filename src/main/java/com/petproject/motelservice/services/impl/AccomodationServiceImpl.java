package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.AccomodationsDto;
import com.petproject.motelservice.domain.dto.AllRoomDto;
import com.petproject.motelservice.domain.dto.ImageDto;
import com.petproject.motelservice.domain.dto.OtherFeesDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Images;
import com.petproject.motelservice.domain.inventory.OtherFees;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.payload.response.DropDownAccomodation;
import com.petproject.motelservice.domain.query.response.RoomFeeResponse;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.OtherFeeRepository;
import com.petproject.motelservice.repository.RoomFeeRepository;
import com.petproject.motelservice.repository.UsersRepository;
import com.petproject.motelservice.services.AccomodationService;
import com.petproject.motelservice.services.UserServices;

@Service
public class AccomodationServiceImpl implements AccomodationService {
	
	@Autowired
	AccomodationsRepository accomodationsRepository;
	
	@Autowired
	OtherFeeRepository otherFeeRepository;
	
	@Autowired
	UserServices userService;
	
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	RoomFeeRepository roomFeeRepository;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public AccomodationsDto createOrUpdate(AccomodationsDto request) {
		Accomodations accomodations = null;
		Date createAt = null;
		AccomodationsDto result = new AccomodationsDto();
		Users user = new Users();
		user.setId(request.getUserId());
		List<OtherFees> feesList = new ArrayList<>();
		if (request.getId() == null) {
			accomodations = new Accomodations();
			createAt = new Date();
		} else {
			accomodations = accomodationsRepository.findById(request.getId()).orElse(null);
			createAt = accomodations.getCreateAt();
		}
		
		accomodations = mapper.map(request, Accomodations.class);
		if (createAt != null) {
			accomodations.setCreateAt(createAt);
		}
		accomodations.setUser(user);
		accomodations = accomodationsRepository.save(accomodations);
		if (!request.getOtherFees().isEmpty() && request.getId() == null) {
			List<OtherFeesDto> fees = request.getOtherFees();
			OtherFees fee = null;
			for (OtherFeesDto item : fees) {
				fee = new OtherFees();
				fee.setAccomodations(accomodations);
				fee.setName(item.getName());
				fee.setPrice(item.getPrice());
				fee.setUnit(item.getUnit());
				feesList.add(fee);
			}
		}
		if (!feesList.isEmpty()) {
			otherFeeRepository.saveAll(feesList);
		}
		result = mapper.map(accomodations, AccomodationsDto.class);
		return result;
	}

	@Override
	public List<AllRoomDto> getAll() {
		List<Accomodations> accomodations = accomodationsRepository.findAll();
		List<Rooms> rooms = null;
		List<Images> images = null;
		AllRoomDto dto = null;
		List<AllRoomDto> result = new ArrayList<>();
		List<RoomFeeResponse> fees = null;
		ImageDto imageDto;
		List<ImageDto> imageResult = new ArrayList<>();
		for (Accomodations item : accomodations) {
			rooms = item.getRooms();
			for (Rooms room : rooms) {
				dto = new AllRoomDto();
				dto.setAccomodationId(item.getId());
				dto.setPrice(room.getPrice());
				dto.setId(room.getId());
				dto.setElectricPrice(item.getElectricPrice());
				dto.setWaterPrice(item.getWaterPrice());
				dto.setAddress(item.getAddress());
				dto.setAccomodationName(item.getName());
				
				fees = roomFeeRepository.findByRoom(room.getId());
				dto.setFees(fees);
				
				images = room.getImages();
				imageResult = new ArrayList<>();
				for (Images img : images) {
					imageDto = new ImageDto();
					imageDto.setImageId(img.getId());
					imageDto.setImgName(img.getImageName());
					imageDto.setImgUrl(img.getImageUrl());
					imageResult.add(imageDto);
				}
				dto.setImages(imageResult);
				result.add(dto);
			}
		}
		return result;
	}

	@Override
	public void removeAccomodation(Integer id) {
		Accomodations accomodations = accomodationsRepository.findById(id).orElse(null);
		if (accomodations != null) {
			List<OtherFees> fees = accomodations.getFees();
			otherFeeRepository.deleteAll(fees);
			accomodationsRepository.delete(accomodations);
		}
	}

	@Override
	public List<AccomodationsDto> getAccomodationByUserId(Integer userId) {
		Users users = usersRepository.findByUserId(userId);
		List<Accomodations> accomodations = accomodationsRepository.findByUser(users);
		TypeMap<Accomodations, AccomodationsDto> typeMap = mapper.getTypeMap(Accomodations.class, AccomodationsDto.class);
		if (typeMap == null) {
			typeMap = mapper.createTypeMap(Accomodations.class, AccomodationsDto.class);
			typeMap.addMapping(Accomodations::getFees, AccomodationsDto::setOtherFees);
		}
		
		List<AccomodationsDto> result = accomodations.stream()
                .map(source -> mapper.map(source, AccomodationsDto.class))
                .collect(Collectors.toList());
		return result;
	}

	@Override
	public List<DropDownAccomodation> getDropdownAccomodationByUserId(Integer userId) {
		Users users = usersRepository.findByUserId(userId);
		List<Accomodations> accomodations = accomodationsRepository.findByUser(users);
		List<DropDownAccomodation> result = accomodations.stream()
                .map(source -> mapper.map(source, DropDownAccomodation.class))
                .collect(Collectors.toList());
		return result;
	}
	
}
