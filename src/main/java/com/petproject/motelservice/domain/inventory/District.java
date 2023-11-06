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
@Table(name = "district")
@Getter
@Setter
public class District extends BaseEntity{

	@Column(name = "district")
	private String district;

	@Column(name = "district_code")
	private Integer districtCode;
	
	@OneToMany(mappedBy = "district", fetch = FetchType.LAZY)
	private List<Ward> wards;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "province_id", nullable = false)
	private Province province;
}
