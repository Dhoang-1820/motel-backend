package com.petproject.motelservice.domain.inventory;

import java.util.Date;
import java.util.List;

import com.petproject.motelservice.framework.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post")
@Getter @Setter
public class Post extends BaseEntity {
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "content", columnDefinition = "TEXT")
    private String content;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "last_change")
	private Date lastChange;
	
	@Column(name = "is_active", columnDefinition = "BOOLEAN")
	private Boolean isActive;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users user;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	 private Rooms room;
	
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Images> images;
	
	@OneToMany(mappedBy = "id.post", fetch = FetchType.LAZY)
    private List<PostUtitlities> postUtitlities;
	
}
