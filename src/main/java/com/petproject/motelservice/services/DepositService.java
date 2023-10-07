package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.DepositDto;

public interface DepositService {
	
	List<DepositDto> getDepositByAccomodation(Integer accomodationId);
	
	List<DepositDto> saveDeposit(DepositDto request);
}
