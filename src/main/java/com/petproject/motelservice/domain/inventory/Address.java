package com.petproject.motelservice.domain.inventory;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Column(name = "ward")
	private String ward;

	@Column(name = "ward_code")
	private Integer wardCode;

	@Column(name = "district")
	private String district;

	@Column(name = "district_code")
	private Integer districtCode;

	@Column(name = "province")
	private String province;

	@Column(name = "province_code")
	private Integer provinceCode;

	@OneToOne(mappedBy = "address")
	private Accomodations accomodation;

}
