package com.petproject.motelservice.domain.payload.request;

import com.petproject.motelservice.domain.inventory.EPostStatus;

import lombok.Data;

@Data
public class PostStatusRequest {
	
	private EPostStatus status;
	
	private Integer postId;
}
