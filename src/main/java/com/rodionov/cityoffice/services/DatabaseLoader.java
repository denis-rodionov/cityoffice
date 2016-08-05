package com.rodionov.cityoffice.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.UserRepository;

@Service
public class DatabaseLoader {
	
	@Autowired
	private UserRepository userRepository;

    @PostConstruct
    private void initDatabase() {
    	
    	if (userRepository.findAll().size() == 0) {
    		
    		User defaultUser = new User();
    		defaultUser.setEmail("admin@admin.com");
    		defaultUser.setPassword("admin");
    		defaultUser.setRole("ADMIN");
    		
    		userRepository.insert(defaultUser);
    	}
    }
}