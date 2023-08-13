package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.Tenants;

public interface TenantRepository extends JpaRepository<Tenants, Integer> {

}
