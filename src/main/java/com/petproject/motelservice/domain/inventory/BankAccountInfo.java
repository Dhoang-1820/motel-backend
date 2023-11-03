package com.petproject.motelservice.domain.inventory;

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
@Table(name = "bank_account_info")
@Getter @Setter
public class BankAccountInfo extends BaseEntity{
	
	@Column(name = "bank_number")
	private String bankNumber;
	
	@Column(name = "bank_name")
	private String bankName;
	
	@Column(name = "account_owner")
	private String accountOwner;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users user;
}
