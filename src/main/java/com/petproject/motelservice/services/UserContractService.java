package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.ContractDto;

public interface UserContractService {
	
	List<ContractDto> getContractByAccomodation(Integer accommodationId);
	
	List<ContractDto> saveContract(ContractDto request);
	
}
