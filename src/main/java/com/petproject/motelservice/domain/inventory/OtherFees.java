package com.petproject.motelservice.domain.inventory;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "other_fees")
@Getter @Setter
public class OtherFees {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "price")
	private Double price;
	
	@Column(name = "unit")
	private String unit;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accomodation_id")
	private Accomodations accomodations;
	
	@OneToMany(mappedBy = "id.fee", fetch = FetchType.LAZY)
	private List<RoomFees> fees;

	public OtherFees(Integer id) {
		this.id = id;
	}

	public OtherFees() {
	}
	
}
