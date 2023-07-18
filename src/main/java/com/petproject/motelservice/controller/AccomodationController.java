package com.petproject.motelservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petproject.motelservice.repository.AccomodationsRepository;

@RestController
@RequestMapping(value = "/accomodation")
public class AccomodationController {
	
	@Autowired
	AccomodationsRepository accomodationsRepository;

	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> registerUser() {
		return ResponseEntity.ok(accomodationsRepository.findAll());
	}
}
