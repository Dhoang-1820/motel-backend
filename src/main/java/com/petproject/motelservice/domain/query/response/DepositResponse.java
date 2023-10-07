package com.petproject.motelservice.domain.query.response;

import java.util.Date;

public interface DepositResponse {
	
	Integer getId();
	
	Date getCreatedAt();
	
	Double getDeposit();
	
	Date getDueDate();
	
	Boolean getIsActive();
	
	Date getStartDate();
	
	Integer getTenantId();
	
	String getFirstName();
	
	String getLastName();
	
	String getPhone();
	
	String getIdentifyNum();
	
	String getEmail();

	Boolean getIsRepaid();
	
	Integer getRoomId();
	
	String getRoomName();
	
	String getNote();
	
	Integer getAccomodationId();
	
}
