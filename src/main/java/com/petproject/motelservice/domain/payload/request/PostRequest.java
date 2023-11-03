package com.petproject.motelservice.domain.payload.request;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.domain.dto.ContractServiceDto;
import com.petproject.motelservice.domain.payload.response.RoomResponse;

import lombok.Data;

@Data
public class PostRequest {
	
	private Integer id;

	private String title;

	private String content;
	
	private Boolean isActive;
	
	private Date createdAt;
	
	private Date lastChange;
	
	private Integer userId;
	
	private RoomResponse room;
	
	private List<ContractServiceDto> services;
}
