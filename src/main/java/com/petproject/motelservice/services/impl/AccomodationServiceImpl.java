package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.AccomodationUtilitiesDto;
import com.petproject.motelservice.domain.dto.AccomodationsDto;
import com.petproject.motelservice.domain.inventory.AccomodationUtilities;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Address;
import com.petproject.motelservice.domain.inventory.District;
import com.petproject.motelservice.domain.inventory.Province;
import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.domain.inventory.Ward;
import com.petproject.motelservice.domain.payload.response.DropDownAccomodation;
import com.petproject.motelservice.repository.AccomodationServiceRepository;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.AddressRepository;
import com.petproject.motelservice.repository.DistrictRepository;
import com.petproject.motelservice.repository.ProvinceRepository;
import com.petproject.motelservice.repository.UsersRepository;
import com.petproject.motelservice.repository.WardRepository;
import com.petproject.motelservice.services.AccomodationService;
import com.petproject.motelservice.services.UserService;

@Service
public class AccomodationServiceImpl implements AccomodationService {
	
	@Autowired
	AccomodationsRepository accomodationsRepository;
	
	@Autowired
	AccomodationServiceRepository serviceRepository;	
	@Autowired
	UserService userService;
	
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	AccomodationServiceRepository accomodationServiceRepository;
	
	@Autowired
	WardRepository wardRepository;
	
	@Autowired
	DistrictRepository districtRepository;
	
	@Autowired
	ProvinceRepository provinceRepository;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public List<AccomodationsDto> createOrUpdate(AccomodationsDto request) {
		Accomodations accomodation = null;
		Address address = null;
		List<AccomodationsDto> result = new ArrayList<>();
		Users user = usersRepository.findByUserId(request.getUserId());
		if (request.getId() == null) {
			accomodation = new Accomodations();
			address = new Address();
			accomodation.setCreateAt(new Date());
			accomodation.setUser(user);
		} else {
			accomodation = accomodationsRepository.findById(request.getId()).orElse(null);
			address = accomodation.getAddress();
			if (address == null) {
				address = new Address();
			}
		}
		
		accomodation.setName(request.getName());
		
		Ward ward = wardRepository.findByWardCode(request.getWardCode());
		if (ward == null) {
			ward = new Ward();
			ward.setWard(request.getWard());
			ward.setWardCode(request.getWardCode());
			District district = districtRepository.findByDistrictCode(request.getDistrictCode());
			if (district == null) {
				district = new District();
				district.setDistrict(request.getDistrict());
				district.setDistrictCode(request.getDistrictCode());
				Province province = provinceRepository.findByProvinceCode(request.getProvinceCode());
				if (province == null) {
					province = new Province();
					province.setProvince(request.getProvince());
					province.setProvinceCode(request.getProvinceCode());
					province = provinceRepository.save(province);
				}
				district.setProvince(province);
				district = districtRepository.save(district);
			}
			ward.setDistrict(district);
			ward = wardRepository.save(ward);
		}
		address.setAddressLine(request.getAddressLine());
		address.setWard(ward);
		accomodation.setAddress(address);
		accomodation = accomodationsRepository.save(accomodation);
		List<AccomodationUtilitiesDto> services = request.getServices();
		for (AccomodationUtilitiesDto service : services) {
			service.setAccomodationId(accomodation.getId());
			saveService(service);			
		}
		result = getAccomodationByUserId(request.getUserId());
		return result;
	}
	
	@Override
	public void removeAccomodation(Integer id) {
		Accomodations accomodations = accomodationsRepository.findById(id).orElse(null);
		if (accomodations != null) {
			accomodationsRepository.delete(accomodations);
		}
	}
	
	private AccomodationsDto convert2Dto(Accomodations accomodation, List<AccomodationUtilities> utilities) {
		AccomodationsDto dto = new AccomodationsDto();
		AccomodationUtilitiesDto utilitiesDto = null;
		List<AccomodationUtilitiesDto> utilitiesResult = new ArrayList<>();
		Address address = accomodation.getAddress();
		if (address != null) {
			Ward ward = address.getWard();
			District district = ward.getDistrict();
			Province province = district.getProvince();
			dto.setAddressLine(address.getAddressLine());
			dto.setDistrict(district.getDistrict());
			dto.setDistrictCode(district.getDistrictCode());
			dto.setProvince(province.getProvince());
			dto.setProvinceCode(province.getProvinceCode());
			dto.setWard(ward.getWard());
			dto.setWardCode(ward.getWardCode());
		}
		dto.setId(accomodation.getId());
		dto.setName(accomodation.getName());
		
		for (AccomodationUtilities utility : utilities) {
			utilitiesDto = new AccomodationUtilitiesDto();
			utilitiesDto.setAccomodationId(accomodation.getId());
			utilitiesDto.setDescription(utility.getDescription());
			utilitiesDto.setId(utility.getId());
			utilitiesDto.setName(utility.getName());
			utilitiesDto.setPrice(utility.getPrice());
			utilitiesDto.setUnit(utility.getUnit());
			utilitiesDto.setIsDefault(utility.getIsDefault());
			utilitiesResult.add(utilitiesDto);
		}
		dto.setServices(utilitiesResult);
		return dto;
	}

	@Override
	public List<AccomodationsDto> getAccomodationByUserId(Integer userId) {
		List<Accomodations> accomodations = accomodationsRepository.findByUserId(userId);
		List<AccomodationsDto> result = new ArrayList<>();
		List<AccomodationUtilities> services = null;
		for (Accomodations accomodation : accomodations) {
			services = accomodationServiceRepository.findByAccomodationIdAndIsDefault(accomodation.getId(), true);
			result.add(convert2Dto(accomodation, services));
		}
		return result;
	}

	@Override
	public List<DropDownAccomodation> getDropdownAccomodationByUserId(Integer userId) {
		List<Accomodations> accomodations = accomodationsRepository.findByUserId(userId);
		List<DropDownAccomodation> result = accomodations.stream()
                .map(source -> mapper.map(source, DropDownAccomodation.class))
                .collect(Collectors.toList());
		return result;
	}

	@Override
	public List<AccomodationUtilitiesDto> getServiceByAccomodation(Integer accomodationId) {
		List<AccomodationUtilitiesDto> result = new ArrayList<>();
		AccomodationUtilitiesDto dto = null;
		List<AccomodationUtilities> services = serviceRepository.findByAccomodationId(accomodationId);
		for (AccomodationUtilities item : services) {
			dto = new AccomodationUtilitiesDto();
			dto.setAccomodationId(accomodationId);
			dto.setId(item.getId());
			dto.setName(item.getName());
			dto.setUnit(item.getUnit());
			dto.setPrice(item.getPrice());
			dto.setDescription(item.getDescription());
			dto.setIsDefault(item.getIsDefault());
			result.add(dto);
		}
		return result;
	}
	
	@Override
	public List<AccomodationUtilitiesDto> saveService(AccomodationUtilitiesDto request) {
		List<AccomodationUtilitiesDto> result = new ArrayList<>();
		AccomodationUtilities service = null;
		Accomodations accomodations = accomodationsRepository.findById(request.getAccomodationId()).orElse(null);
		if (request.getId() == null) {
			service = new AccomodationUtilities();
		} else {
			service = serviceRepository.findById(request.getId()).orElse(null);
		}
		service.setAccomodation(accomodations);
		service.setDescription(request.getDescription());
		service.setName(request.getName());
		service.setPrice(request.getPrice());
		service.setUnit(request.getUnit());
		service.setIsDefault(request.getIsDefault());
		serviceRepository.save(service);
		result = getServiceByAccomodation(request.getAccomodationId());
		return result;
	}
	
	@Override
	public Boolean checkServiceValid(AccomodationUtilitiesDto request) {
		Boolean result = false;
		List<AccomodationUtilities> services = serviceRepository.findByNameAndAccomodationId(request.getName(), request.getAccomodationId());
		if (services.isEmpty()) {
			result = true;
		}
		return result;
	}
}








