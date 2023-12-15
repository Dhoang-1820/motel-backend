package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.DepositDto;
import com.petproject.motelservice.domain.payload.request.CancelDepositRequest;

public interface DepositService {
	
	List<DepositDto> getDepositByAccomodation(Integer accomodationId);
	
	Boolean saveDeposit(DepositDto request);
	
	Boolean cancelDeposit(CancelDepositRequest request);
}
