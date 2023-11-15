package com.petproject.motelservice.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.dto.EquipmentDto;
import com.petproject.motelservice.domain.inventory.Accomodations;
import com.petproject.motelservice.domain.inventory.Equipments;
import com.petproject.motelservice.domain.inventory.Rooms;
import com.petproject.motelservice.domain.payload.response.RoomResponse;
import com.petproject.motelservice.domain.query.response.EquipmentsResponse;
import com.petproject.motelservice.repository.AccomodationsRepository;
import com.petproject.motelservice.repository.EquipmentRepository;
import com.petproject.motelservice.repository.RoomRepository;
import com.petproject.motelservice.services.EquipmentService;

@Service
public class EquipmentServiceImpl implements EquipmentService {

	@Autowired
	EquipmentRepository equipmentRepository;

	@Autowired
	AccomodationsRepository accomodationRepository;

	@Autowired
	RoomRepository roomRepository;

	@Override
	public List<EquipmentDto> getByAccomodationId(Integer accomodationId) {
		List<EquipmentsResponse> equipments = equipmentRepository.findByAccomodationId(accomodationId);
		EquipmentDto equipment = null;
		List<EquipmentDto> result = new ArrayList<>();
		for (EquipmentsResponse item : equipments) {
			equipment = new EquipmentDto();
			equipment.setId(item.getId());
			equipment.setDescription(item.getDescription());
			equipment.setName(item.getName());
			equipment.setPrice(item.getPrice());
			equipment.setQuantity(item.getQuantity());
			equipment.setRoomId(null);
			equipment.setUnit(item.getUnit());
			equipment.setStatus(item.getStatus());
			equipment.setAccomodationId(accomodationId);
			result.add(equipment);
		}

		return result;
	}
	
	@Override
	public Boolean checkDuplicated(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EquipmentDto> getByRoomId(Integer roomId) {
		List<EquipmentDto> result = new ArrayList<>();
		List<Equipments> equipments = equipmentRepository.findByRoomId(roomId);
		EquipmentDto equipment = null;
		for (Equipments item : equipments) {
			equipment = new EquipmentDto();
			equipment.setId(item.getId());
			equipment.setDescription(item.getDescription());
			equipment.setName(item.getName());
			equipment.setPrice(item.getPrice());
			equipment.setRoomId(roomId);
			equipment.setUnit(item.getUnit());
			equipment.setStatus(item.getStatus());
			result.add(equipment);
		}
		return result;
	}
	
	@Override
	public List<EquipmentDto> getByName(String name) {
		List<EquipmentDto> result = new ArrayList<>();
		List<Equipments> equipments = equipmentRepository.findByName(name);
		EquipmentDto equipment = null;
		Rooms room = null;
		Integer roomId;
		String roomName;
		Double price;
		for (Equipments item : equipments) {
			equipment = new EquipmentDto();
			equipment.setId(item.getId());
			equipment.setDescription(item.getDescription());
			equipment.setName(item.getName());
			equipment.setPrice(item.getPrice());
			room = item.getRoom();
			roomId = null;
			roomName = null;
			price = null;
			if (room != null) {
				roomId = room.getId();
				roomName = room.getName();
				price = room.getPrice();
			} 
			equipment.setRoomId(roomId);				
			equipment.setUnit(item.getUnit());
			equipment.setStatus(item.getStatus());
			equipment.setRoom(new RoomResponse(roomId, roomName, price, null));
			result.add(equipment);
		}
		return result;
	}

	@Override
	public List<EquipmentDto> saveEquipment(List<EquipmentDto> requests) {
		Integer accomodationId = requests.get(0).getAccomodationId();
		Equipments equipment = null;
		Rooms room = null;
		Accomodations accomodation = null;
		for (EquipmentDto request : requests) {
			if (request.getId() == null) {
				equipment = new Equipments();
			} else {
				equipment = equipmentRepository.findById(request.getId()).orElse(null);
			}
			equipment.setDescription(request.getDescription());
			equipment.setName(request.getName());
			equipment.setPrice(request.getPrice());
			equipment.setStatus(request.getStatus());
			equipment.setUnit(request.getUnit());
			if (request.getRoomId() != null) {
				room = roomRepository.findById(request.getRoomId()).orElse(null);
				equipment.setRoom(room);
			} else {
				equipment.setRoom(null);
			}
			accomodation = accomodationRepository.findById(accomodationId).orElse(null);
			equipment.setAccomodations(accomodation);
			equipmentRepository.save(equipment);
		}
		return getByAccomodationId(accomodationId);
	}

	@Override
	public void deleteEquipment(Integer id) {
		try {
			Equipments equipment = equipmentRepository.findById(id).orElse(null);
			equipmentRepository.delete(equipment);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
