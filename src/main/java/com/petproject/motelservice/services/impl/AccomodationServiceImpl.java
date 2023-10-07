package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.AccomodationUtilitiesDto;
import com.petproject.motelservice.domain.dto.AccomodationsDto;
import com.petproject.motelservice.domain.dto.AllRoomDto;
import com.petproject.motelservice.domain.dto.ImageDto;
import com.petproject.motelservice.domain.inventory.AccomodationUtilities;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Address;
import com.petproject.motelservice.domain.inventory.Images;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.payload.response.DropDownAccomodation;
import com.petproject.motelservice.domain.query.response.RoomFeeResponse;
import com.petproject.motelservice.repository.AccomodationServiceRepository;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.AddressRepository;
import com.petproject.motelservice.repository.UsersRepository;
import com.petproject.motelservice.services.AccomodationService;
import com.petproject.motelservice.services.UserServices;

@Service
public class AccomodationServiceImpl implements AccomodationService {
	
	@Autowired
	AccomodationsRepository accomodationsRepository;
	
	@Autowired
	AccomodationServiceRepository serviceRepository;	
	@Autowired
	UserServices userService;
	
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public List<AccomodationsDto> createOrUpdate(AccomodationsDto request) {
		Accomodations accomodations = null;
		Address address = null;
		List<AccomodationsDto> result = new ArrayList<>();
		Users user = new Users();
		user.setId(request.getUserId());
		if (request.getId() == null) {
			accomodations = new Accomodations();
			address = new Address();
			accomodations.setCreateAt(new Date());
		} else {
			accomodations = accomodationsRepository.findById(request.getId()).orElse(null);
			address = accomodations.getAddress();
		}
		
		accomodations.setUser(user);
		accomodations.setName(request.getName());
		address.setAddressLine(request.getAddressLine());
		address.setWard(request.getWard());
		address.setWardCode(request.getWardCode());
		address.setDistrict(request.getDistrict());
		address.setDistrictCode(request.getDistrictCode());
		address.setProvince(request.getProvince());
		address.setProvinceCode(request.getProvinceCode());
		accomodations.setAddress(address);
		accomodations = accomodationsRepository.save(accomodations);
		address.setAccomodations(accomodations);
		address = addressRepository.save(address);
		result = getAccomodationByUserId(request.getUserId());
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
//				dto.setElectricPrice(item.getElectricPrice());
//				dto.setWaterPrice(item.getWaterPrice());
//				dto.setAddress(item.getAddress());
				dto.setAccomodationName(item.getName());
				
				dto.setFees(fees);
				
//				images = room.getImages();
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
			accomodationsRepository.delete(accomodations);
		}
	}
	
	private AccomodationsDto convert2Dto(Accomodations accomodation) {
		AccomodationsDto dto = new AccomodationsDto();
		Address address = accomodation.getAddress();
		dto.setId(accomodation.getId());
		dto.setName(accomodation.getName());
		dto.setDistrict(address.getDistrict());
		dto.setDistrictCode(address.getDistrictCode());
		dto.setProvince(address.getProvince());
		dto.setProvinceCode(address.getProvinceCode());
		dto.setWard(address.getWard());
		dto.setWardCode(address.getWardCode());
		dto.setAddressLine(address.getAddressLine());
		return dto;
	}

	@Override
	public List<AccomodationsDto> getAccomodationByUserId(Integer userId) {
		List<Accomodations> accomodations = accomodationsRepository.findByUserId(userId);
		List<AccomodationsDto> result = new ArrayList<>();
		for (Accomodations accomodation : accomodations) {
			result.add(convert2Dto(accomodation));
		}
		return result;
	}

	@Override
	public List<DropDownAccomodation> getDropdownAccomodationByUserId(Integer userId) {
		List<Accomodations> accomodations = accomodationsRepository.findByUserId(userId);
		List<DropDownAccomodation> result = accomodations.stream()
                .map(source -> mapper.map(source, DropDownAccomodation.class))
                .collect(Collectors.toList());
		return result;
	}

	@Override
	public List<AccomodationUtilitiesDto> getServiceByAccomodation(Integer accomodationId) {
		List<AccomodationUtilitiesDto> result = new ArrayList<>();
		AccomodationUtilitiesDto dto = null;
		List<AccomodationUtilities> services = serviceRepository.findByAccomodationId(accomodationId);
		for (AccomodationUtilities item : services) {
			dto = new AccomodationUtilitiesDto();
			dto.setAccomodationId(accomodationId);
			dto.setId(item.getId());
			dto.setName(item.getName());
			dto.setUnit(item.getUnit());
			dto.setPrice(item.getPrice());
			dto.setDescription(item.getDescription());
			result.add(dto);
		}
		return result;
	}
	
	@Override
	public List<AccomodationUtilitiesDto> saveService(AccomodationUtilitiesDto request) {
		List<AccomodationUtilitiesDto> result = new ArrayList<>();
		AccomodationUtilities service = null;
		Accomodations accomodations = accomodationsRepository.findById(request.getAccomodationId()).orElse(null);
		if (request.getId() == null) {
			service = new AccomodationUtilities();
		} else {
			service = serviceRepository.findById(request.getId()).orElse(null);
		}
		service.setAccomodation(accomodations);
		service.setDescription(request.getDescription());
		service.setName(request.getName());
		service.setPrice(request.getPrice());
		service.setUnit(request.getUnit());
		serviceRepository.save(service);
		result = getServiceByAccomodation(request.getAccomodationId());
		return result;
	}
}








