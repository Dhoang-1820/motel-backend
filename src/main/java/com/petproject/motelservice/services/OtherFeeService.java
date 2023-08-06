package com.petproject.motelservice.services;

import com.petproject.motelservice.domain.dto.OtherFeesDto;

public interface OtherFeeService {
	
	public OtherFeesDto createOrUpdate(OtherFeesDto feesDto);
	
	public void removeFee(Integer id);
}
