package com.petproject.motelservice.domain.inventory;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class PostUtilitiesId implements Serializable {

	private static final long serialVersionUID = -6781302496338765878L;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
	private AccomodationUtilities accomodationService;

}
