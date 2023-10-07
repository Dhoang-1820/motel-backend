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

@Entity
@Table(name = "bills")
@Getter @Setter
public class Bills extends BaseEntity{

	@Column(name = "date")
	private Date billDate;
	
	@Column(name = "total_price")
	private Double totalPrice;
	
	@Column(name = "is_pay", columnDefinition = "BOOLEAN")
	private Boolean isPay;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "is_sent", columnDefinition = "BOOLEAN")
	private Boolean isSent;
	
	@Column(name = "paid_money")
	private Double paidMoney;
	
	@Column(name = "old_debt")
	private Double oldDebt;
	
	@Column(name = "new_debt")
	private Double newDebt;
	
	@Column(name = "total_payment")
	private Double totalPayment;
	
	@Column(name = "quantity_sent")
	private Integer quantitySent;
	
	@Column(name = "note")
	private String note;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Rooms room;
}
