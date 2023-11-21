package com.petproject.motelservice.security.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petproject.motelservice.domain.inventory.Users;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String username;

	private String email;
	
	private Boolean isActive;
	
	private String firstname;
	
	private String lastname;
	
	private String identifyNum;
	
	private String phone;

	@JsonIgnore
	private String password;

	private GrantedAuthority authority;

	public static UserDetailsImpl build(Users user) {
		GrantedAuthority authorities = new SimpleGrantedAuthority(user.getRole().getName().name());
		return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getActive(), user.getFirstname(), user.getLastname(), user.getIdentifyNum(), authorities, user.getPassword(), user.getPhone());
	}
	
	public UserDetailsImpl(Integer id, String username, String email, Boolean isActive, String firstname,
			String lastname, String identifyNum, GrantedAuthority authority, String password, String phone) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.isActive = isActive;
		this.firstname = firstname;
		this.lastname = lastname;
		this.identifyNum = identifyNum;
		this.authority = authority;
		this.password = password;
		this.phone = phone;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdentifyNum() {
		return identifyNum;
	}

	public void setIdentifyNum(String identifyNum) {
		this.identifyNum = identifyNum;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(authority);
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

}
