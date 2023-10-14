package com.petproject.motelservice.domain.dto;

import com.petproject.motelservice.domain.payload.response.RoomResponse;

import lombok.Data;

@Data
public class EquipmentDto {

	private Integer id;
	
	private String name;
	
	private String description;
	
	private String status;
	
	private Double price;
	
	private Integer quantity;
	
	private String unit;
	
	private Integer roomId;
	
	private RoomResponse room;
	
	private Boolean isAddRoom;
	
	private Integer accomodationId;
}
