package com.petproject.motelservice.domain.dto;

import java.util.List;

import com.petproject.motelservice.domain.query.response.RoomFeeResponse;
import com.petproject.motelservice.domain.query.response.RoomServiceResponse;

import lombok.Data;

@Data
public class RoomServiceDto {

	private RoomServiceResponse room;

	private List<RoomFeeResponse> fees;
}
