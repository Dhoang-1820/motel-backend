package com.petproject.motelservice.domain.query.response;

import java.util.Date;

public interface UserResponse {

	Integer getUserId();

	String getFirstName();

	String getAddress();

	String getPhone();

	String getEmail();

	String getLastName();

	Boolean getActive();

	Date getCreatedAt();

	String getRole();
	
	String getIdentifyNum();

	Integer getAccomodationNum();
}
