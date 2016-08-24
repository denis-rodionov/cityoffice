package com.rodionov.cityoffice.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mysema.query.BooleanBuilder;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.model.QProject;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.ProjectRepository;
import com.rodionov.cityoffice.repository.UserRepository;

@Service
public class ProjectService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	/**
	 * Finds out which users must be notified about the document deadline regardles about double notifications
	 * @param doc Document to notify about
	 * @return List of users
	 */
	public List<User> getUsersToNotify(String projectId) {
		return userRepository.findAll()
				.stream()
				.filter(u -> {
					List<String> projectIds = u.getProjectIds();
					
					if (projectIds == null)
						return false;
					
					return projectIds.stream().anyMatch(p -> p.equals(projectId));
				})
				.collect(Collectors.toList());
	}

	/**
	 * Git project by given filters
	 * @param projectIds
	 * @param name
	 * @param isActive
	 * @param pageable
	 * @return
	 */
	public Page<Project> getFilteredProjects(List<String> projectIds, String name, Boolean isActive,
			Pageable pageable) {
		
		QProject project = new QProject("project");
		BooleanBuilder where = new BooleanBuilder();
		
		if (projectIds != null) {
			where.and(project.id.in(projectIds));
		}
		
		if (name != null) {
			where.and(project.name.containsIgnoreCase(name));
		}
		
		if (isActive != null) {
			where.and(project.isActive.eq(isActive));
		}
		
		return projectRepository.findAll(where, pageable);
	}

	/**
	 * Check if the project with given data exists in the database
	 * @param project
	 * @return True if such project exists in database
	 */
	public boolean exists(Project project) {
		return projectRepository.findByName(project.getName()).stream().findAny().orElse(null) != null;
	}

	/**
	 * Gets Project by given id
	 * @param projectId
	 * @return
	 */
	public Project getProject(String projectId) {
		return projectRepository.findOne(projectId);
	}
}
