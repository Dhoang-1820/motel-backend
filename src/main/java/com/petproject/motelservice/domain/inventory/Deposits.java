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
@Table(name = "deposit")
public class Deposits extends BaseEntity {
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "due_date")
	private Date dueDate;
	
	@Column(name = "deposit")
	private Double deposit;
	
	@Column(name = "note")
	private String note;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "is_active", columnDefinition = "BOOLEAN")
	private Boolean isActive = Boolean.TRUE;
	
	@Column(name = "is_repaid", columnDefinition = "BOOLEAN")
	private Boolean isRepaid = Boolean.FALSE;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Rooms room;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tenant_id")
	private Tenants tenant;
	
}
