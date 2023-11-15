package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.ContractDto;
import com.petproject.motelservice.domain.dto.ContractServiceDto;
import com.petproject.motelservice.domain.dto.TenantDto;
import com.petproject.motelservice.domain.inventory.AccomodationUtilities;
import com.petproject.motelservice.domain.inventory.Contract;
import com.petproject.motelservice.domain.inventory.ContractService;
import com.petproject.motelservice.domain.inventory.ContractServiceId;
import com.petproject.motelservice.domain.inventory.Deposits;
import com.petproject.motelservice.domain.inventory.Post;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.inventory.Tenants;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.repository.AccomodationServiceRepository;
import com.petproject.motelservice.repository.ContractRepository;
import com.petproject.motelservice.repository.ContractServiceRepository;
import com.petproject.motelservice.repository.DepositRepository;
import com.petproject.motelservice.repository.PostRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.repository.TenantRepository;
import com.petproject.motelservice.services.UserContractService;

@Service
public class UserContractServiceImpl implements UserContractService {
	
	@Autowired
	ContractRepository contractRepository;
	
	@Autowired
	DepositRepository depositRepository;
	
	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	AccomodationServiceRepository accomodationServiceRepository;
	
	@Autowired
	ContractServiceRepository contractServiceRepository;
	
	@Autowired
	TenantRepository tenantRepository;
	
	@Autowired
	PostRepository postRepository;
	
	private static final Log logger = LogFactory.getLog(UserContractServiceImpl.class);

	@Override
	public List<ContractDto> getContractByAccomodation(Integer accommodationId) {
		List<Contract> contracts = contractRepository.findByRoomAccomodationsIdAndIsActive(accommodationId, Boolean.TRUE);
		List<ContractDto> result = new ArrayList<>();
		for (Contract contract : contracts) {
			result.add(convert2ContractDto(contract));
		}
		return result;
	}
	
	private ContractDto convert2ContractDto(Contract contract) {
		ContractDto dto = null;
		Rooms room = null;
		RoomResponse roomResponse = null;
		dto = new ContractDto();
		dto.setId(contract.getId());
		dto.setDuration(contract.getDuration());
		dto.setDeposit(contract.getDeposits());
		dto.setStartDate(contract.getStartDate());
		dto.setEndDate(contract.getEndDate());
		dto.setFirstElectricNum(contract.getFirstElectricNum());
		dto.setFirstWaterNum(contract.getFirstWaterNum());
		dto.setIsActive(contract.getIsActive());
		dto.setDayStayedBefore(contract.getDayStayedBefore());
		dto.setFirstComePayment(contract.getFirstComePayment());
		dto.setKeepRoomDeposit(contract.getKeepRoomDeposit());
		if (contract.getRepresentative() != null) {
			Tenants representative = tenantRepository.findById(contract.getRepresentative()).orElse(null);
			if (representative != null) {
				dto.setRepresentative(new TenantDto(representative.getId(), representative.getFirstName(), representative.getLastName()));							
			}
		}
		room = contract.getRoom();
		roomResponse = new RoomResponse(room.getId(), room.getName(), room.getPrice(), room.getCapacity());
		dto.setRoom(roomResponse);
		dto.setServices(convert2ServiceDto(contract.getContractServices()));
		dto.setTenants(convert2TenantDto(contract.getTenants()));
		return dto;
	}
 	
	private List<TenantDto> convert2TenantDto(List<Tenants> tenants) {
		TenantDto dto = null;
		 List<TenantDto> result = new ArrayList<>();
		 for (Tenants tenant : tenants) {
			 dto = new TenantDto();
			 dto.setId(tenant.getId());
			 dto.setFirstName(tenant.getFirstName());
			 dto.setLastName(tenant.getLastName());
			 result.add(dto);
		}
		 return result;
	}
	
	private List<ContractServiceDto> convert2ServiceDto(List<ContractService> services) {
		ContractServiceDto contractSerivce = null;
		List<ContractServiceDto> contractSerivces = new ArrayList<>();
		AccomodationUtilities accomodationUtilities = null;
		for (ContractService service : services) {
			contractSerivce = new ContractServiceDto();
			accomodationUtilities = service.getId().getAccomodationService();
			contractSerivce.setId(accomodationUtilities.getId());
			contractSerivce.setUnit(accomodationUtilities.getUnit());
			contractSerivce.setName(accomodationUtilities.getName());
			contractSerivce.setQuantity(service.getQuantity());
			contractSerivces.add(contractSerivce);
		}
		return contractSerivces;
	}

	@Override
	public List<ContractDto> saveContract(ContractDto request) {
		Contract contract = null;
		Rooms room = null;
        Deposits deposit = null;
		try {
			if (request.getId() != null) {
				contract = contractRepository.findById(request.getId()).orElse(null);
			} else {
				contract = new Contract();
			}
			
			contract.setDeposits(request.getDeposit());
			contract.setDuration(request.getDuration());
			contract.setStartDate(request.getStartDate());
			contract.setEndDate(request.getEndDate());
			contract.setFirstElectricNum(request.getFirstElectricNum());
			contract.setFirstWaterNum(request.getFirstWaterNum());
			contract.setRepresentative(request.getRepresentative().getId());
			contract.setDayStayedBefore(request.getDayStayedBefore());
			contract.setFirstComePayment(request.getFirstComePayment());
			contract.setKeepRoomDeposit(request.getKeepRoomDeposit());
			deposit = depositRepository.findByRoomId(request.getRoom().getId());
            if (deposit != null) {
                deposit.setIsActive(Boolean.FALSE);
                depositRepository.save(deposit);
            }
			room = roomRepository.findById(request.getRoom().getId()).orElse(null);
			room.setIsRent(Boolean.TRUE);
			roomRepository.save(room);
			contract.setRoom(room);
			contract = contractRepository.save(contract);
			if (request.getPreRoom() != null && request.getPreRoom() != request.getRoom().getId()) {
				room = roomRepository.findById(request.getPreRoom()).orElse(null);
				room.setIsRent(Boolean.FALSE);
				roomRepository.save(room);
			}
            
			saveContractService(request.getServices(), contract);
			saveTenant(request.getTenants(), contract);
			List<Post> posts = postRepository.findByRoomIdAndIsActive(room.getId(), true);
			for (Post post : posts) {
				post.setIsActive(false);
				postRepository.save(post);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void saveTenant(List<TenantDto> tenants, Contract contract) {
		Tenants tenant = null;
		int updatedRows = tenantRepository.updateTenantContractStatus(contract.getId());
		logger.info("updatedRows: " + updatedRows);
		for (TenantDto tenantDto : tenants) {
			tenant = tenantRepository.findById(tenantDto.getId()).orElse(null);
			tenant.setStartDate(contract.getStartDate());
			tenant.setContract(contract);
			tenantRepository.save(tenant);
		}
	}
	
	private void saveContractService(List<ContractServiceDto> contractService, Contract contract) {
		ContractService service = null;
		ContractServiceId serviceId = null;
		AccomodationUtilities accomodationUtilities = null;
		int updatedRows = contractServiceRepository.deleteByIdContractId(contract.getId());
		logger.info("deletedRows: " + updatedRows);
		for (ContractServiceDto item : contractService) {
			service = new ContractService();
			serviceId = new ContractServiceId();
			accomodationUtilities = accomodationServiceRepository.findById(item.getId()).orElse(null);
			serviceId.setContract(contract);
			serviceId.setAccomodationService(accomodationUtilities);
			service.setId(serviceId);
			service.setQuantity(item.getQuantity());
			contractServiceRepository.save(service);
		}
	}

}
