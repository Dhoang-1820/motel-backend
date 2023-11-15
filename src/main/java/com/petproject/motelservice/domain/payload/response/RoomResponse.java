package com.petproject.motelservice.domain.payload.response;

import java.util.Objects;

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
	
	private Double price;
	
	private Integer capacity;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoomResponse other = (RoomResponse) obj;
		return Objects.equals(id, other.id);
	}
	
}
