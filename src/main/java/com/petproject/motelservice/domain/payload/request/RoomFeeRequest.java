package com.petproject.motelservice.domain.payload.request;

import lombok.Data;

@Data
public class RoomFeeRequest {
	
	private Integer feeId;
	
	private Integer roomId;
	
	private Integer quantity;
}
