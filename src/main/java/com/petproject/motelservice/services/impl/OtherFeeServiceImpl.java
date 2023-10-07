package com.petproject.motelservice.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.AccomodationUtilitiesDto;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.services.OtherFeeService;

@Service
public class OtherFeeServiceImpl implements OtherFeeService {
	
	@Autowired
	AccomodationsRepository accomodationsRepository;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public AccomodationUtilitiesDto createOrUpdate(AccomodationUtilitiesDto request) {
//		OtherFees otherFees = null;
//		OtherFeesDto result = new OtherFeesDto();
//		Accomodations accomodations = accomodationsRepository.findById(request.getAccomodationId()).orElse(null);
//		if (request.getId() == null) {
//			otherFees = new OtherFees();
//		} else {
//			otherFees = otherFeeRepository.findById(request.getId()).orElse(null);
//		}
//		otherFees.setAccomodations(accomodations);
//		otherFees.setName(request.getName());
//		otherFees.setPrice(request.getPrice());
//		otherFees.setUnit(request.getUnit());
//		otherFees = otherFeeRepository.save(otherFees);
//		result = mapper.map(otherFees, OtherFeesDto.class);
//		result.setAccomodationId(accomodations.getId());
		return null;
	}

	@Override
	public void removeFee(Integer id) {
//		OtherFees otherFees = otherFeeRepository.findById(id).orElse(null);
//		if (otherFees != null) {
//			List<RoomFees> roomfees = otherFees.getFees();
//			if (!roomfees.isEmpty()) {
//				roomFeeRepository.deleteAll(roomfees);
//			}
//			otherFeeRepository.delete(otherFees);
//		}
	}
	
}
