package com.petproject.motelservice.services;

import com.petproject.motelservice.domain.dto.AccomodationUtilitiesDto;

public interface OtherFeeService {
	
	public AccomodationUtilitiesDto createOrUpdate(AccomodationUtilitiesDto feesDto);
	
	public void removeFee(Integer id);
}
