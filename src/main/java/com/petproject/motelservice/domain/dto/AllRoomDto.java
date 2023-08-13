package com.petproject.motelservice.domain.dto;import java.util.List;

import com.petproject.motelservice.domain.query.response.RoomFeeResponse;

import lombok.Data;

@Data
public class  AllRoomDto {
	
	private Integer id;
	
	private Integer accomodationId;
	
	private Double price;
	
	private Double electricPrice;
	
	private Double waterPrice;
	
	private String address;
	
	private String accomodationName;
	
	private List<RoomFeeResponse> fees;
	
	private List<ImageDto> images;

}
