package com.petproject.motelservice.domain.query.response;

public interface RoomFeeResponse {

	public Integer getFeeId();
	
	public Integer getQuantity();

	public Integer getRoomId();
	
	public String getRoomName();
	
	public String getFeeName();
	
	public String getUnit();

	public Double getPrice();
	
	public Double getTotal();
	
}
