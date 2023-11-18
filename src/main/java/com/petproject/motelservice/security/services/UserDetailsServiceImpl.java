package com.petproject.motelservice.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.petproject.motelservice.domain.inventory.Users;
import com.petproject.motelservice.repository.UsersRepository;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UsersRepository usersRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Users user = usersRepository.findByUsername(userName).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + userName);
		} 
		return UserDetailsImpl.build(user);
	}

}
