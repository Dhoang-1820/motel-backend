package com.petproject.motelservice.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.OtherFeesDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.OtherFees;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.OtherFeeRepository;
import com.petproject.motelservice.services.OtherFeeService;

@Service
public class OtherFeeServiceImpl implements OtherFeeService {
	
	@Autowired 
	OtherFeeRepository otherFeeRepository;
	
	@Autowired
	AccomodationsRepository accomodationsRepository;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public OtherFeesDto createOrUpdate(OtherFeesDto request) {
		OtherFees otherFees = null;
		OtherFeesDto result = new OtherFeesDto();
		Accomodations accomodations = accomodationsRepository.findById(request.getAccomodationId()).orElse(null);
		if (request.getId() == null) {
			otherFees = new OtherFees();
		} else {
			otherFees = otherFeeRepository.findById(request.getId()).orElse(null);
		}
		otherFees.setAccomodations(accomodations);
		otherFees.setName(request.getName());
		otherFees.setPrice(request.getPrice());
		otherFees.setUnit(request.getUnit());
		otherFees = otherFeeRepository.save(otherFees);
		result = mapper.map(otherFees, OtherFeesDto.class);
		result.setAccomodationId(accomodations.getId());
		return result;
	}

	@Override
	public void removeFee(Integer id) {
		OtherFees otherFees = otherFeeRepository.findById(id).orElse(null);
		if (otherFees != null) {
			otherFeeRepository.delete(otherFees);
		}
	}
	
}
