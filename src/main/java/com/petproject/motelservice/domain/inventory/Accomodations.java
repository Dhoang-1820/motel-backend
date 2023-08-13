package com.petproject.motelservice.domain.inventory;

import java.util.Date;
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
@Table(name = "accomodations")
@Getter @Setter
public class Accomodations {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "electric_price")
	private Double electricPrice;
	
	@Column(name = "water_price")
	private Double waterPrice;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "created_at")
	private Date createAt;
	
	@Column(name = "active", columnDefinition = "BOOLEAN")
	private Boolean isActive = Boolean.TRUE;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users user;
	
	@OneToMany(mappedBy = "accomodations", fetch = FetchType.LAZY)
	private List<Rooms> rooms;
	
	@OneToMany(mappedBy = "accomodations", fetch = FetchType.LAZY)
	private List<OtherFees> fees;

	public Accomodations(Integer id) {
		this.id = id;
	}

	public Accomodations() {
	}
	
}
