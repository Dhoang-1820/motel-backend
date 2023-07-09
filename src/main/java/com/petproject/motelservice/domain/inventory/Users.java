package com.petproject.motelservice.domain.inventory;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "username")
	private String userName;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "phone")
	private Integer phone;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "active", columnDefinition = "BOOLEAN")
	private Boolean active =  Boolean.TRUE;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Images> images;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Accomodations> accomodations;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Post> post;
	
	@Enumerated(EnumType.ORDINAL)
	private Roles role;
}
