package com.rodionov.cityoffice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.model.UserProject;
import com.rodionov.cityoffice.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public List<UserProject> getAllUsersProjects() {
		return null;
	}
}
