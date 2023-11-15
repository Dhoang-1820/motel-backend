package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.TenantDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Tenants;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.DepositRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.repository.TenantRepository;
import com.petproject.motelservice.services.TenantsService;

@Service
public class TenantsServiceImpl implements TenantsService {
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	AccomodationsRepository accomodationsRepository;
	
	@Autowired
	TenantRepository tenantRepository;
	
	@Autowired
	DepositRepository depositRepository;

	@Override
	public List<TenantDto> getTenantByAccomodation(Integer id) {
		List<Tenants> tenants = tenantRepository.findByAccomodationsId(id);
		List<TenantDto> result = new ArrayList<>();
		for (Tenants tenant : tenants) {
			result.add(convert2Dto(tenant));
		}
		return result;
	}
	
	
	@Override
	public List<Tenants> countTenantByUserId(Integer userId) {
		return tenantRepository.countTenantByUserId(userId);
	}


	@Override
	public Boolean checkDuplicated(String identifyNum) {
		Boolean result = false;
		Tenants tenant = tenantRepository.findByIdentifyNum(identifyNum);
		if (tenant != null) {
			result = true;
		}
		return result;
	}

	@Override
	public TenantDto createOrUpdate(TenantDto request) {
		Tenants tenant = null;
		if (request.getId() != null) {
			tenant = tenantRepository.findById(request.getId()).orElse(null);
		} else {
			tenant = new Tenants();
		}
		tenant.setFirstName(request.getFirstName());
		tenant.setLastName(request.getLastName());
		tenant.setIdentifyNum(request.getIdentifyNum());
		tenant.setIsStayed(request.getIsStayed());
		tenant.setStartDate(request.getStartDate());
		tenant.setPhone(request.getPhone());
		tenant.setEmail(request.getEmail());
		tenant.setGender(request.getGender());
		if (request.getAccomodationId() != null) {
			Accomodations accomodations = accomodationsRepository.findById(request.getAccomodationId()).orElse(null);
			tenant.setAccomodations(accomodations);
		}
		tenant = tenantRepository.save(tenant);
		return null;
	}

	private TenantDto convert2Dto(Tenants tenant) {
		TenantDto tenantDto = new TenantDto();
		tenantDto.setId(tenant.getId());
		tenantDto.setEmail(tenant.getEmail());
		tenantDto.setFirstName(tenant.getFirstName());
		tenantDto.setLastName(tenant.getLastName());
		tenantDto.setIdentifyNum(tenant.getIdentifyNum());
		tenantDto.setPhone(tenant.getPhone());
		tenantDto.setStartDate(tenant.getStartDate());
		tenantDto.setEndDate(tenant.getEndDate());
		tenantDto.setIsStayed(tenant.getIsStayed());
		tenantDto.setGender(tenant.getGender());
		return tenantDto;
	}

	@Override
	public List<TenantDto> getTenantNotDeposit(Integer id) {
		List<Tenants> tenants = tenantRepository.findTenantWithoutDeposit(id);
		List<TenantDto> result = new ArrayList<>();
		for (Tenants tenant : tenants) {
			result.add(convert2Dto(tenant));
		}
		return result;
	}

	@Override
	public List<TenantDto> getTenantNotContract(Integer id) {
		List<Tenants> tenants = tenantRepository.findTenantNotContracted(id);
		List<TenantDto> result = new ArrayList<>();
		for (Tenants tenant : tenants) {
			result.add(convert2Dto(tenant));
		}
		return result;
	}

}
