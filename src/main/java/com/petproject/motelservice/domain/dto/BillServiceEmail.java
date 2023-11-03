package com.petproject.motelservice.domain.dto;

import lombok.Data;

@Data
public class BillServiceEmail {
	
	String serviceName;
	
	String unit;
	
	String price;
		
	Integer quantity;
		
	Integer firstElectricNum;
	
	Integer lastElectricNum;
	
	Integer firstWaterNum;
	
	Integer lastWaterNum;
	
	Integer electricNum;
	
	Integer waterNum;
	
	String totalPrice;		
}
