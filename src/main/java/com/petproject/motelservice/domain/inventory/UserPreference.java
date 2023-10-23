package com.petproject.motelservice.domain.inventory;

import java.util.Date;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_preference")
@Getter @Setter
public class UserPreference extends BaseEntity {
	
	@Column(name = "email_date")
	private Date emailDate;
	
	@Column(name = "issue_invoice_date")
	private Date issueInvoiceDate;
	
	@Column(name = "remind_date")
	private Date remindDate;
	
	@OneToOne(mappedBy = "userPreference")
	private Users user;
}
