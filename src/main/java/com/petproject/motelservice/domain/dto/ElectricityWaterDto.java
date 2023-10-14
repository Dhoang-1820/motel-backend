package com.petproject.motelservice.domain.dto;

import java.util.Date;

import com.petproject.motelservice.domain.payload.response.RoomResponse;

import lombok.Data;

@Data
public class ElectricityWaterDto {

	private Integer id;

	private Date month;

	private Integer firstElectric;

	private Integer lastElectric;

	private Integer electricNum;

	private Integer firstWater;

	private Integer lastWater;

	private Integer waterNum;

	private Date createdAt;

	private RoomResponse room;
	
	private Integer accomodationId;
}
