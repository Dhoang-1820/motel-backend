package com.petproject.motelservice.domain.inventory;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "refresh_token")
@Getter @Setter
public class RefreshToken extends BaseEntity{
	
	@Column(nullable = false, unique = true)
	private String token;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private Users user;
}
