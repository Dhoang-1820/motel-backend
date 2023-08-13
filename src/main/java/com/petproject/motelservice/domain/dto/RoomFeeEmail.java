package com.petproject.motelservice.domain.dto;

import lombok.Data;

@Data
public class RoomFeeEmail {
	
	private Integer quantity;

	private Integer feeId;
	
	private String feeName;
	
	private String unit;
	
	private String price;
	
	private String total;

	public RoomFeeEmail(Integer quantity, Integer feeId, String feeName, String unit, String price, String total) {
		this.quantity = quantity;
		this.feeId = feeId;
		this.feeName = feeName;
		this.unit = unit;
		this.price = price;
		this.total = total;
	}
	
}
