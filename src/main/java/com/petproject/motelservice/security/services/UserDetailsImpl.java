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

	@JsonIgnore
	private String password;

	private GrantedAuthority authority;

	public static UserDetailsImpl build(Users user) {
		GrantedAuthority authorities = new SimpleGrantedAuthority(user.getRole().getName().name());

		return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities, user.getActive());
	}

	public UserDetailsImpl(Integer id, String username, String email, String password,
			GrantedAuthority authority, Boolean isActive) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authority = authority;
		this.isActive = isActive;
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
