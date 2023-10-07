package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}
