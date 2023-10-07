package com.petproject.motelservice.domain.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
@NoArgsConstructor
public class RoomResponse {
	
	private Integer id;
	
	private String name;
}
