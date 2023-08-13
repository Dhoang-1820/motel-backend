package com.petproject.motelservice.domain.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class RoomFees {
	
	@EmbeddedId
	private RoomFeeId id;
	
	@Column(name = "quantity")
	private int quantity;
}
