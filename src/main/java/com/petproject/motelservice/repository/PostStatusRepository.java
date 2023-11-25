package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.EPostStatus;
import com.petproject.motelservice.domain.inventory.PostStatus;

public interface PostStatusRepository extends JpaRepository<PostStatus, Integer> {

	PostStatus findByName(EPostStatus name);
}
