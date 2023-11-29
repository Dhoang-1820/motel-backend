package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.EquipmentDto;

public interface EquipmentService {
	
	List<EquipmentDto> getByAccomodationId(Integer accomodationId);
	
	List<EquipmentDto> saveEquipment(List<EquipmentDto> request);
	
	void deleteEquipment(Integer id);

	List<EquipmentDto> getByRoomId(Integer roomId);

	List<EquipmentDto> getByNameAndAccomodation(String name, Integer accomodationId);
	
	Boolean checkDuplicated(String name);
}
