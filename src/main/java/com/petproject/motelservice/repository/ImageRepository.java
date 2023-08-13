package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.Images;

public interface ImageRepository extends JpaRepository<Images, Integer> {

}
