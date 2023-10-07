package com.petproject.motelservice.services;

import java.util.List;

import com.petproject.motelservice.domain.dto.RoomServiceDto;
import com.petproject.motelservice.domain.payload.request.RoomFeeRequest;
import com.petproject.motelservice.domain.query.response.OtherFeesResponse;


public interface RoomFeeService {
	
	public List<RoomServiceDto> getRoomFeeByAccomodation(Integer accomodation);
	
	public List<OtherFeesResponse> getRemainFeesDropDown(Integer roomId, Integer accomodationId);
	
	public void saveRoomFee(RoomFeeRequest request);
	
	public void removeRoomFee(Integer feeId, Integer roomId);
}
