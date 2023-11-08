package com.petproject.motelservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petproject.motelservice.domain.inventory.UserPreference;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Integer> {
	
	@Query("SELECT userPreference FROM UserPreference userPreference WHERE day(userPreference.issueInvoiceDate) = day(:date) AND month(userPreference.issueInvoiceDate) = month(:date) AND year(userPreference.issueInvoiceDate) = year(:date)")
	List<UserPreference> findIssueDateByDate(Date date);
	
	@Query("SELECT userPreference FROM UserPreference userPreference WHERE day(userPreference.remindDate) = day(:date) AND month(userPreference.remindDate) = month(:date) AND year(userPreference.remindDate) = year(:date)")
	List<UserPreference> findRemindByDate(Date date);
}
