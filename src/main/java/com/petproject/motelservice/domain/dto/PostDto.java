package com.petproject.motelservice.domain.dto;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.petproject.motelservice.domain.inventory.EPostStatus;

import lombok.Data;

@Data
public class PostDto {

	private Integer id;
	
	private String title;
	
	private String content;
	
	private Double acreage;
	
	private Double price;
	
	private Integer capacity;
	
	private Integer addressId;
	
	private String address;

	private String addressLine;
	
	private String ward;

	private Integer wardCode;
	
	private String district;
	
	private Integer districtCode;
	
	private String province;
	
	private Integer provinceCode;
	
	private String phone;
	
	private Date createdAt;
	
	private Boolean isActive;
	
	private EPostStatus status;
	
	private List<ImageDto> images;

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
