package com.petproject.motelservice.domain.dto;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PostDto other = (PostDto) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
}
