package com.petproject.motelservice.domain.inventory;

import java.util.List;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoice_type")
@Getter @Setter
public class InvoiceType extends BaseEntity{
	
	@Column(name = "type")
	private String type;
	
	@OneToMany(mappedBy = "invoiceType", fetch = FetchType.LAZY)
	private List<Bills> bill;
}
