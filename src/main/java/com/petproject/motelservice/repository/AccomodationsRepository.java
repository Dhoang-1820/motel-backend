package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.Accomodations;

public interface AccomodationsRepository extends JpaRepository<Accomodations, Integer> {

}
