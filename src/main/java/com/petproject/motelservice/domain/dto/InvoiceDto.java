package com.petproject.motelservice.domain.dto;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.domain.payload.response.RoomResponse;

import lombok.Data;

@Data
public class InvoiceDto {

	private Integer id;
	
	private RoomResponse room;
	
	private Boolean isSent;
	
	private Double totalPayment;

	private Date billDate;

	private Double totalPrice;

	private Boolean isPay;

	private Date createdAt;
	
	private String representative;
	
	private Integer quantitySent;
	
	private Double paidMoney;
	
	private Double debt;
	
	private Double newDebt;
	
	private Double totalService;
	
	private Double discount;
	
	private Date paymentDate;
	
	private String description;
	
	List<BillServiceDto> service;
	
}
