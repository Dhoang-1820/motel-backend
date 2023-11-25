package com.petproject.motelservice.domain.inventory;

import java.util.List;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ward_id", nullable = false)
	private Ward ward;
	
	@OneToMany(mappedBy = "address")
	private List<Post> post;

}
