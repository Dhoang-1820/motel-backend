package com.petproject.motelservice.domain.inventory;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomFeeId implements Serializable{
	
	private static final long serialVersionUID = -7892714533710057947L;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
	private Rooms room;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_id", nullable = false)
	private OtherFees fee;

	public RoomFeeId(Rooms room) {
		this.room = room;
	}
	
}
