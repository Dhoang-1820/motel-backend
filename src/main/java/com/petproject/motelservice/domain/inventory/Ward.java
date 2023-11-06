package com.petproject.motelservice.domain.inventory;

import java.util.List;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ward")
@Getter
@Setter
public class Ward extends BaseEntity{

	@Column(name = "ward")
	private String ward;

	@Column(name = "ward_code")
	private Integer wardCode;
	
	@OneToMany(mappedBy = "ward", fetch = FetchType.LAZY)
	private List<Address> addresses;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district_id", nullable = false)
	private District district;
}
