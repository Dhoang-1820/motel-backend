package com.petproject.motelservice.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class RoomImageDto {
	
	private Integer roomId;
	
	private List<ImageDto> images;
}
