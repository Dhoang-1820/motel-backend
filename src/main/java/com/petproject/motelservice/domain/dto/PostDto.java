package com.petproject.motelservice.domain.dto;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.domain.payload.response.RoomResponse;

import lombok.Data;

@Data
public class PostDto {

	private Integer id;
	
	private String title;
	
	private String content;
	
	private Double acreage;
	
	private Double price;
	
	private String address;
	
	private String phone;
	
	private Date createdAt;
	
	private Boolean isActive;
	
	private RoomResponse room;
	
	private List<EquipmentDto> equipments;
	
	private List<ImageDto> images;
	
	private List<ContractServiceDto> services;
}
