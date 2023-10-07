package com.petproject.motelservice.domain.dto;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.domain.query.response.RoomFeeResponse;

import lombok.Data;

@Data
public class InvoiceDto {

	private Integer id;
	
	private String name;
	
	private Boolean isSent;
	
	private Integer billId;

	private Date billDate;

	private Integer electricNum;

	private Integer waterNum;

	private Double totalPrice;

	private Boolean isPay;

	private Date createdAt;
	
	private List<RoomFeeResponse> fees;
}
