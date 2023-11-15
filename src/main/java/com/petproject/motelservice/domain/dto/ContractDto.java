package com.petproject.motelservice.domain.dto;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.domain.payload.response.RoomResponse;

import lombok.Data;

@Data
public class ContractDto {

	private Integer id;
	
	private Integer duration;
	
	private Double oldDeposit;
	
	private Double deposit;
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer firstElectricNum;
	
	private Integer firstWaterNum;
	
	private Boolean isActive;
	
	private List<TenantDto> tenants;
	
	private List<ContractServiceDto> services;
	
	private TenantDto representative;
	
	private RoomResponse room;

	private Integer preRoom;
	
	private Double keepRoomDeposit;
	
	private Integer dayStayedBefore;
	
	private Double firstComePayment;
}
