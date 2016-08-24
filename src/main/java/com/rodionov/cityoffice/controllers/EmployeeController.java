package com.rodionov.cityoffice.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import com.rodionov.cityoffice.dto.EmployeeDTO;
import com.rodionov.cityoffice.dto.EmployeeProjectDTO;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.model.UserProject;
import com.rodionov.cityoffice.services.ProjectService;
import com.rodionov.cityoffice.services.UserService;

@RestController
public class EmployeeController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	/**
	 * Get information about employees:
	 *  their projects in the specified period
	 *  their vacancies 
	 * @param periodInDays Period in days from today to the future (example: 180 - half a year)
	 * @return
	 */
	public List<EmployeeDTO> getEmployeePeriodInfo(Integer periodInDays) {

		Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);
		LocalDate finishAfter = LocalDate.now();
		LocalDate startBefore = finishAfter.plusDays(periodInDays);
		List<UserProject> userProjects = userService.getUserProjects(null, null, startBefore, finishAfter, pageable).getContent();
		
		List<EmployeeDTO> employees = userProjects.stream()
					.collect(Collectors.groupingBy(UserProject::getUserId))
					.entrySet()
					.stream()
					.map(entry -> convertToDto(entry.getKey(), entry.getValue()))
					.collect(Collectors.toList());
		
		return employees;
	}
	
	private EmployeeDTO convertToDto(String userId, List<UserProject> projects) {
		EmployeeDTO dto = new EmployeeDTO();
		dto.setId(userId);
		dto.setName(userService.getUser(userId).getUsername());
		dto.setProjects(projects.stream().map(this::convertToProjectDTO).collect(Collectors.toList()));
		
		return dto;
	}
	
	private EmployeeProjectDTO convertToProjectDTO(UserProject userProject) {
		EmployeeProjectDTO dto = new EmployeeProjectDTO();
		dto.setId(userProject.getProjectId());
		dto.setName(projectService.getProject(userProject.getProjectId()).getName());
		dto.setStartDate(userProject.getStartDate());
		dto.setFinishDate(userProject.getFinishDate());
		
		return dto;
	}
}
