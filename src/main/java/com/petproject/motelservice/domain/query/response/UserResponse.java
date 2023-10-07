package com.petproject.motelservice.domain.query.response;

import java.util.Date;

public interface UserResponse {
	
	public Integer getUserId();
	
	public String getFirstName();
	
	public String getAddress();
	
	public String getPhone();
	
	public String getEmail();
	
	public String getLastName();
	
	public Boolean getActive();
	
	public Date getCreatedAt();
	
	public Integer getAccomodationNum();
}
