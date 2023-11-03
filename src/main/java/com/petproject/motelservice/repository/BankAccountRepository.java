package com.petproject.motelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petproject.motelservice.domain.inventory.BankAccountInfo;

public interface BankAccountRepository extends JpaRepository<BankAccountInfo, Integer> {

}
