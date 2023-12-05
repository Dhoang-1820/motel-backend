package com.petproject.motelservice.domain.inventory;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tenants")
@Getter @Setter
public class Tenants extends BaseEntity{
	
	@Column(name = "identify_num")
	private String identifyNum;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "is_stayed", columnDefinition = "BOOLEAN")
	private Boolean isStayed;
	
	@Column(name = "is_active", columnDefinition = "BOOLEAN")
	private Boolean isActive = Boolean.TRUE;
	
	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private EGender gender;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id")
	private Contract contract;
	
	@OneToMany(mappedBy = "tenant")
	private List<Deposits> deposit;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accomodation_id")
	private Accomodations accomodations;
}
