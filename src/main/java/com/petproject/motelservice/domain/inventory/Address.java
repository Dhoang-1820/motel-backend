package com.petproject.motelservice.domain.inventory;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address extends BaseEntity {

	@Column(name = "adress_line")
	private String addressLine;

	@OneToOne(mappedBy = "address")
	private Accomodations accomodation;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "post_id", nullable = false)
//	private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ward_id", nullable = false)
	private Ward ward;

}
