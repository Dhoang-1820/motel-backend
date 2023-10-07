package com.petproject.motelservice.domain.payload.request;

import lombok.Data;

@Data
public class CancelDepositRequest {

	private Integer depositId;
	
	private Boolean isRepaid;
}
