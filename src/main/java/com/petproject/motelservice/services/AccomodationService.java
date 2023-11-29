package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.AccomodationUtilitiesDto;
import com.petproject.motelservice.domain.dto.AccomodationsDto;
import com.petproject.motelservice.domain.payload.response.DropDownAccomodation;

public interface AccomodationService {

	List<AccomodationsDto> createOrUpdate(AccomodationsDto accomodations);

	List<AccomodationsDto> getAccomodationByUserId(Integer userId);

	Boolean removeAccomodation(Integer id);
	
	Boolean removeService(Integer id);
	
	Boolean isCanRemoveAccomodation(Integer id);
	
	Boolean isCanRemoveAccomodationService(AccomodationUtilitiesDto request);

	List<DropDownAccomodation> getDropdownAccomodationByUserId(Integer userId);
	
	List<AccomodationUtilitiesDto> getServiceByAccomodation(Integer accomodationId);

	List<AccomodationUtilitiesDto> saveService(AccomodationUtilitiesDto request);

	Boolean checkServiceValid(AccomodationUtilitiesDto request);
}
