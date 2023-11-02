package com.petproject.motelservice.domain.inventory;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "post_utilities")
public class PostUtitlities {

	@EmbeddedId
	private PostUtilitiesId id;
	
}
