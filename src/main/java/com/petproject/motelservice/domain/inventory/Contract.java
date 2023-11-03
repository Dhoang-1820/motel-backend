package com.petproject.motelservice.domain.inventory;

import java.util.Date;
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
@Table(name = "contract")
@Getter @Setter
public class Contract extends BaseEntity {
	
	@Column(name = "representative")
	private Integer representative;
	
	@Column(name = "duration")
	private Integer duration;
	
	@Column(name = "recurrent")
	private Integer recurrent;
	
	@Column(name = "deposits")
	private Double deposits;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "first_electric_num")
	private Integer firstElectricNum;
	
	@Column(name = "first_water_num")
	private Integer firstWaterNum;
	
	@Column(name = "is_active", columnDefinition = "BOOLEAN")
	private Boolean isActive = Boolean.TRUE;;
	
	@OneToMany(mappedBy = "id.contract", fetch = FetchType.LAZY)
	private List<ContractService> contractServices;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", nullable = false)
	private Rooms room;
	
	@OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
	private List<Tenants> tenants;
	
}
