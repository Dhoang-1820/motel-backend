package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.petproject.motelservice.domain.inventory.PostUtilitiesId;
import com.petproject.motelservice.domain.inventory.PostUtitlities;

public interface PostUtilitiesRepository extends JpaRepository<PostUtitlities, PostUtilitiesId> {
	
	@Modifying
	@Transactional
	int deleteByIdPostId(Integer contractId);
}
