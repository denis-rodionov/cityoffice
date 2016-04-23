package com.rodionov.cityoffice.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.UserRepository;

@Service
public class ProjectService {
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Finds out which users must be notified about the document deadline
	 * @param doc Document to notify about
	 * @return List of users
	 */
	public List<User> getUsersToNotify(String projectId) {
		return userRepository.findAll()
				.stream()
				.filter(u -> u.getProjectIds().stream().anyMatch(p -> p.equals(projectId)))
				.collect(Collectors.toList());
	}
}
