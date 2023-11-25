package com.petproject.motelservice.domain.inventory;

import java.util.List;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_status")
@Getter @Setter
public class PostStatus extends BaseEntity {

	@Enumerated(EnumType.STRING)
	private EPostStatus name;
	
	@OneToMany(mappedBy = "postStatus", fetch = FetchType.LAZY)
	List<Post> post;
}
