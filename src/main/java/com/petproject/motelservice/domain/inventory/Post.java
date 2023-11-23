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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post")
@Getter @Setter
public class Post extends BaseEntity {
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "acreage")
	private Double acreage;
	
	@Column(name = "capacity")
	private Integer capacity;
	
	@Column(name = "price")
	private Double price;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_status_id")
	private PostStatus postStatus;
	
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Images> images;
	
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostUtitlities> postUtitlities;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "address_id")
//	private Address address;
	
}
