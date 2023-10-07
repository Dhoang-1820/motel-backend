package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.AccomodationUtilitiesDto;
import com.petproject.motelservice.domain.dto.AccomodationsDto;
import com.petproject.motelservice.domain.dto.AllRoomDto;
import com.petproject.motelservice.domain.payload.response.DropDownAccomodation;

public interface AccomodationService {

	List<AccomodationsDto> createOrUpdate(AccomodationsDto accomodations);

	List<AllRoomDto> getAll();

	List<AccomodationsDto> getAccomodationByUserId(Integer userId);

	void removeAccomodation(Integer id);

	List<DropDownAccomodation> getDropdownAccomodationByUserId(Integer userId);
	
	List<AccomodationUtilitiesDto> getServiceByAccomodation(Integer accomodationId);

	List<AccomodationUtilitiesDto> saveService(AccomodationUtilitiesDto request);
}
