package com.petproject.motelservice.domain.payload.request;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.domain.dto.ImageDto;
import com.petproject.motelservice.domain.inventory.EPostStatus;

import lombok.Data;

@Data
public class PostRequest {
	
	private Integer id;

	private String title;
	
	private Double acreage;
	
	private Integer capacity;
	
	private Integer emptyRoomNum;
	
	private Double price;

	private String content;
	
	private Boolean isActive;
	
	private Date createdAt;
	
	private Date lastChange;
	
	private Integer userId;
	
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
	
	private EPostStatus status;
	
	private List<ImageDto> images;
}
