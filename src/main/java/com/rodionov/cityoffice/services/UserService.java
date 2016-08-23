package com.rodionov.cityoffice.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mysema.query.BooleanBuilder;
import com.rodionov.cityoffice.model.QUser;
import com.rodionov.cityoffice.model.QUserProject;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.model.UserProject;
import com.rodionov.cityoffice.repository.UserProjectRepository;
import com.rodionov.cityoffice.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserProjectRepository userProjectRepository;

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

	/**
	 * Get all user's projects membership
	 * @param projects	Project where user is member for a period
	 * @param userId	User ID
	 * @param pageable	For pageable request
	 * @param startBefore Get all memberships which start before specified date
	 * @param finishAfter Get all memberships which finish after specified date
	 * @return
	 */
	public Page<UserProject> getUserProjects(
			List<String> projects, 
			String username,			
			LocalDate startBefore,
			LocalDate finishAfter,
			Pageable pageable) {
		
		QUserProject userProject = new QUserProject("userProject");
		BooleanBuilder where = new BooleanBuilder();
		
		if (projects != null && !projects.isEmpty()) {
			where.and(userProject.projectId.in(projects));
		}
		
		if (finishAfter != null) {
			where.and(userProject.finishDate.after(finishAfter));
		}
		
		if (startBefore != null) {
			where.and(userProject.startDate.before(startBefore));
		}
		
		if (username != null) {
			List<String> usersId = userRepository.findByUsernameLikeIgnoreCase(username).stream()
					.map(u -> u.getId())
					.collect(Collectors.toList());
			
			where.and(userProject.userId.in(usersId));
		}
		
		return userProjectRepository.findAll(where, pageable);		
	}

	/**
	 * Checks if the user with given data exists in database
	 * @param user The given user data
	 * @return
	 */
	public boolean exists(User user) {
		User existing = userRepository.findByUsername(user.getUsername());
        User existingByEmail = userRepository.findByEmail(user.getUsername());
        
        return existing == null || existingByEmail != null;
	}
}
