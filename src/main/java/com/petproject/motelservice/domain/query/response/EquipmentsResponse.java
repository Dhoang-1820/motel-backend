package com.petproject.motelservice.domain.query.response;

public interface EquipmentsResponse {
	 Integer getId();
		
	 String getName();
	
	 String getDescription();
	
	 String getStatus();
	 
	 String getUnit();
	
	 Double getPrice();
	
	 Integer getQuantity();
	
	 Integer getRoomId();
	 
	 Integer getAccomodationId();
}
