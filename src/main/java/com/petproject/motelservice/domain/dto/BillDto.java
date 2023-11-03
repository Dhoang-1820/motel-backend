package com.petproject.motelservice.domain.dto;

import java.util.Date;

import com.petproject.motelservice.domain.payload.response.RoomResponse;

import lombok.Data;

@Data
public class BillDto {

	private Integer id;
	
	private Date billDate;
	
	private String representative;
	
	private Double totalPrice;
	
	private Boolean isSent;
	
	private Double oldDebt;
	
	private Double paidMoney;
	
	private Double totalService;
	
	private Integer quantitySented;
	
	private Double discount;
	
	private Double newDebt;
	
	private Double totalPayment;
	
	private Boolean isPay;
	
	private Date createdAt;
	
	private RoomResponse room;
	
	private String note;
}
