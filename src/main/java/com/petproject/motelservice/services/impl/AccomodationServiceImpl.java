package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.AccomodationsDto;
import com.petproject.motelservice.domain.dto.OtherFeesDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.OtherFees;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.payload.response.DropDownAccomodation;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.OtherFeeRepository;
import com.petproject.motelservice.repository.UsersRepository;
import com.petproject.motelservice.services.AccomodationService;
import com.petproject.motelservice.services.UserService;

@Service
public class AccomodationServiceImpl implements AccomodationService {
	
	@Autowired
	AccomodationsRepository accomodationsRepository;
	
	@Autowired
	OtherFeeRepository otherFeeRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public AccomodationsDto createOrUpdate(AccomodationsDto request) {
		Accomodations accomodations = null;
		Date createAt = null;
		AccomodationsDto result = new AccomodationsDto();
		Users user = new Users();
		user.setId(request.getUserId());
		List<OtherFees> feesList = new ArrayList<>();
		if (request.getId() == null) {
			accomodations = new Accomodations();
			createAt = new Date();
		} else {
			accomodations = accomodationsRepository.findById(request.getId()).orElse(null);
			createAt = accomodations.getCreateAt();
		}
		
		accomodations = mapper.map(request, Accomodations.class);
		if (createAt != null) {
			accomodations.setCreateAt(createAt);
		}
		accomodations.setUser(user);
		accomodations = accomodationsRepository.save(accomodations);
		if (!request.getOtherFees().isEmpty() && request.getId() == null) {
			List<OtherFeesDto> fees = request.getOtherFees();
			OtherFees fee = null;
			for (OtherFeesDto item : fees) {
				fee = new OtherFees();
				fee.setAccomodations(accomodations);
				fee.setName(item.getName());
				fee.setPrice(item.getPrice());
				feesList.add(fee);
			}
		}
		if (!feesList.isEmpty()) {
			otherFeeRepository.saveAll(feesList);
		}
		result = mapper.map(accomodations, AccomodationsDto.class);
		return result;
	}

	@Override
	public List<AccomodationsDto> getAll() {
		List<Accomodations> accomodations = accomodationsRepository.findAll();
		List<OtherFees> otherFees = null;
		for (Accomodations item : accomodations) {
			otherFees = item.getFees();
			item.setFees(otherFees);
		}
		TypeMap<Accomodations, AccomodationsDto> typeMap = mapper.getTypeMap(Accomodations.class, AccomodationsDto.class);
		if (typeMap == null) {
			typeMap = mapper.createTypeMap(Accomodations.class, AccomodationsDto.class);
			typeMap.addMapping(Accomodations::getFees, AccomodationsDto::setOtherFees);
		}
		
		List<AccomodationsDto> result = accomodations.stream()
                .map(source -> mapper.map(source, AccomodationsDto.class))
                .collect(Collectors.toList());
		return result;
	}

	@Override
	public void removeAccomodation(Integer id) {
		Accomodations accomodations = accomodationsRepository.findById(id).orElse(null);
		if (accomodations != null) {
			List<OtherFees> fees = accomodations.getFees();
			otherFeeRepository.deleteAll(fees);
			accomodationsRepository.delete(accomodations);
		}
	}

	@Override
	public List<AccomodationsDto> getAccomodationByUserId(Integer userId) {
		Users users = usersRepository.findByUserId(userId);
		List<Accomodations> accomodations = accomodationsRepository.findByUser(users);
		TypeMap<Accomodations, AccomodationsDto> typeMap = mapper.getTypeMap(Accomodations.class, AccomodationsDto.class);
		if (typeMap == null) {
			typeMap = mapper.createTypeMap(Accomodations.class, AccomodationsDto.class);
			typeMap.addMapping(Accomodations::getFees, AccomodationsDto::setOtherFees);
		}
		
		List<AccomodationsDto> result = accomodations.stream()
                .map(source -> mapper.map(source, AccomodationsDto.class))
                .collect(Collectors.toList());
		return result;
	}

	@Override
	public List<DropDownAccomodation> getDropdownAccomodationByUserId(Integer userId) {
		Users users = usersRepository.findByUserId(userId);
		List<Accomodations> accomodations = accomodationsRepository.findByUser(users);
		List<DropDownAccomodation> result = accomodations.stream()
                .map(source -> mapper.map(source, DropDownAccomodation.class))
                .collect(Collectors.toList());
		return result;
	}
	
}
