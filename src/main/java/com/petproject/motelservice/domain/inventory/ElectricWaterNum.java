package com.petproject.motelservice.domain.inventory;

import java.util.Date;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "electric_water_num")
public class ElectricWaterNum extends BaseEntity {
	
	@Column(name = "month")
	private Date month;
	
	@Column(name = "first_electric_num")
	private Integer firstElectric;
	
	@Column(name = "last_electric_num")
	private Integer lastElectric;
	
	@Column(name = "electric_num")
	private Integer electricNum;
	
	@Column(name = "first_water_num")
	private Integer firstWater;
	
	@Column(name = "last_water_num")
	private Integer lastWater;
	
	@Column(name = "water_num")
	private Integer waterNum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", nullable = false)
	private Rooms room;
}
