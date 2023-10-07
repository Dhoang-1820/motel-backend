package com.petproject.motelservice.domain.inventory;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter @Setter
public class Role extends BaseEntity{
	
	@Enumerated(EnumType.STRING)
	private ERoles name;

}
