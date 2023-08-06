package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.AccomodationsDto;
import com.petproject.motelservice.domain.payload.response.DropDownAccomodation;

public interface AccomodationService {

	public AccomodationsDto createOrUpdate(AccomodationsDto accomodations);
	
	public List<AccomodationsDto> getAll();
	
	public List<AccomodationsDto> getAccomodationByUserId(Integer userId);
	
	public void removeAccomodation(Integer id);
	
	public List<DropDownAccomodation> getDropdownAccomodationByUserId(Integer userId);
}
