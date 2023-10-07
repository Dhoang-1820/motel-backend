package com.petproject.motelservice.domain.query.response;

import java.util.Date;

public interface RoomBillResponse {
	
	public Integer getId();
	
	public String getName();
	
	public Boolean getIsSent();
	
	public Integer getElectricNum();
	
	public Integer getWaterNum();
	
	public Integer getBillId();
	
	public Date getMonth();
	
	public Double getTotalPrice();
	
	public Boolean getIsPay();
	
	public Date getCreateAt();
}
