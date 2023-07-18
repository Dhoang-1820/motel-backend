package com.petproject.motelservice.domain.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ApiResponse {
	
	private Boolean success;
	
	private Object data;
	
	private String message;
}
