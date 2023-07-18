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
import jakarta.persistence.ManyToMany;
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
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "active", columnDefinition = "BOOLEAN")
	private Boolean active =  Boolean.TRUE;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "image")
	private String imageUrl;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Accomodations> accomodations;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Post> post;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private List<Roles> role;
}
