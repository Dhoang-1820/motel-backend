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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post")
@Getter @Setter
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "is_approved")
	private Boolean isApproved;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "is_del", columnDefinition = "BOOLEAN")
	private Boolean isDelete = Boolean.FALSE;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Users user;
	
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	List<Images> images;
}
