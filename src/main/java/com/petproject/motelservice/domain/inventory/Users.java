package com.petproject.motelservice.domain.inventory;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
public class Users extends BaseEntity {
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "firstname")
	private String firstname;
	
	@Column(name = "lasname")
	private String lastname;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "identify_num")
	private String identifyNum;
	
	@Column(name = "email")
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private EUserStatus status;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "image")
	private String imageUrl;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Post> posts;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<Accomodations> accomodations;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	List<BankAccountInfo> bankAccountInfos;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_preference_id")
	private UserPreference userPreference;
}
