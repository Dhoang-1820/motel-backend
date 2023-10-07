package com.petproject.motelservice.domain.query.response;

import java.util.Date;

public interface RoomServiceResponse {
	
	public Integer getRoomId();

	public String getRoom();

	public Integer getNumPerson();

	public Date getStartDate();
	
	public Double getTotalPrice();
}
