package com.rodionov.cityoffice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mysema.query.BooleanBuilder;
import com.rodionov.cityoffice.model.QUser;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public Page<User> getFilteredUsers(String username, String projectId, String role, String email, Pageable pageable) {
		
		QUser user = new QUser("user");
		BooleanBuilder where = new BooleanBuilder();
		
		if (username != null) {
			where.and(user.username.containsIgnoreCase(username));
		}
		if (projectId != null) {
			where.and(user.projectIds.contains(projectId));
		}
		if (role != null) {
			where.and(user.role.eq(role));
		}
		if (email != null) {
			where.and(user.email.containsIgnoreCase(email));
		}
		
		return userRepository.findAll(where, pageable);
		
	}
}
