package com.rodionov.cityoffice.model.helpers;

import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.repository.ProjectRepository;

public class ProjectHelper {
	public static String createAndSave(String name, ProjectRepository projectRepository) {
		Project prj = new Project(name, true, "default");
		
		prj = projectRepository.save(prj);
		
		return prj.getId();
	}
}
