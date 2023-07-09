package com.petproject.motelservice.domain.inventory;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bills")
@Getter @Setter
public class Bills {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "date")
	private Date billDate;
	
	@Column(name = "electric_num")
	private Integer electricNum;
	
	@Column(name = "water_num")
	private Integer waterNum;
	
	@Column(name = "total_price")
	private Double totalPrice;
	
	@Column(name = "is_pay", columnDefinition = "BOOLEAN")
	private Boolean isPay;
	
	@Column(name = "created_at")
	private Date createdAt;
}
