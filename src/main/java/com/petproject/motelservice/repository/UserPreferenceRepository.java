package com.petproject.motelservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petproject.motelservice.domain.inventory.UserPreference;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Integer> {
	
	@Query("SELECT userPreference FROM UserPreference userPreference WHERE day(userPreference.issueInvoiceDate) = day(:date)")
	List<UserPreference> findIssueDateByDate(Date date);
	
	@Query("SELECT userPreference FROM UserPreference userPreference WHERE day(userPreference.eletricWaterDate) = day(:date) AND month(userPreference.eletricWaterDate) = month(:date) AND year(userPreference.eletricWaterDate) = year(:date)")
	List<UserPreference> findRemindByDate(Date date);
}
