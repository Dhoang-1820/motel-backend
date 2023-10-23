package com.petproject.motelservice.domain.dto;

import lombok.Data;

@Data
public class BillServiceDto {
	
	String serviceName;
		
	String unit;
	
	Double price;
		
	Integer quantity;
		
	Integer firstElectricNum;
	
	Integer lastElectricNum;
	
	Integer firstWaterNum;
	
	Integer lastWaterNum;
	
	Integer electricNum;
	
	Integer waterNum;
	
	Double totalPrice;		
}
