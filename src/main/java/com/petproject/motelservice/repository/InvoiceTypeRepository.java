package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.InvoiceType;

public interface InvoiceTypeRepository extends JpaRepository<InvoiceType, Integer>{

}
