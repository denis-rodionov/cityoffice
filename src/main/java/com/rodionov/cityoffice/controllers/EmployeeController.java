package com.rodionov.cityoffice.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rodionov.cityoffice.dto.EmployeeDTO;
import com.rodionov.cityoffice.dto.EmployeeProjectDTO;
import com.rodionov.cityoffice.dto.EmployeeVacationDTO;
import com.rodionov.cityoffice.model.UserProject;
import com.rodionov.cityoffice.model.UserVacation;
import com.rodionov.cityoffice.services.ProjectService;
import com.rodionov.cityoffice.services.UserService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	private static final Logger logger = Logger.getLogger(EmployeeController.class);
	
	private static final Integer DEFAULT_PERIOD = 180;

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
	@RequestMapping(method = RequestMethod.GET)
	public List<EmployeeDTO> getEmployeesList(
			@RequestParam(required=false) Integer periodInDays,
			@RequestParam(required=false) String userId,
			@RequestParam(required=false) Boolean onlyProjects,
			@RequestParam(required=false) Boolean onlyVacations) {
		
		logger.info("getEmployeePeriodInfo invoked. (periodInDays = " + periodInDays + ")");
		
		if (periodInDays == null) {
			periodInDays = DEFAULT_PERIOD;
		}

		Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);
		LocalDate finishAfter = LocalDate.now();
		LocalDate startBefore = finishAfter.plusDays(periodInDays);
		
		List<EmployeeDTO> employees = new ArrayList<EmployeeDTO>();
		
		if (onlyVacations == null || !onlyVacations) {
			
			List<UserProject> userProjects = userService.getUserProjects(null, userId, startBefore, finishAfter, pageable).getContent();
			System.out.println("Projects: " + userProjects.size());
			mergeDTOs(employees, getEmployeesWithProjects(userProjects));			
		}
		
		if (onlyProjects == null || !onlyProjects) {
			List<UserVacation> userVacations = userService.getUserVacations(userId, startBefore, finishAfter, pageable).getContent();
			System.out.println("Vacations: " + userVacations.size());
			mergeDTOs(employees, getEmployeesWithVacations(userVacations));
		}
		
		return employees;
	}
	
	private void mergeDTOs(List<EmployeeDTO> dest, List<EmployeeDTO> source) {
				
		System.out.println("Source: " + source.size());
		for (EmployeeDTO cur : source) {
			EmployeeDTO existing = dest.stream().filter(e -> e.getId().equals(cur.getId())).findAny().orElse(null);
			if (existing != null) {
				existing.getProjects().addAll(cur.getProjects());
				existing.getVacations().addAll(cur.getVacations());
			}
			else {
				dest.add(cur);
			}
		}
		System.out.println("Result: " + dest.size());
	}

	private List<EmployeeDTO> getEmployeesWithVacations(List<UserVacation> userVacations) {
		
		return userVacations.stream()				
				.collect(Collectors.groupingBy(UserVacation::getUserId))
				.entrySet()
				.stream()
				.map(entry -> convertToEmployeeDto(entry.getKey(), null, entry.getValue()))
				.collect(Collectors.toList());
	}

	private List<EmployeeDTO> getEmployeesWithProjects(List<UserProject> userProjects) {
		
		return userProjects.stream()
					.collect(Collectors.groupingBy(UserProject::getUserId))
					.entrySet()
					.stream()
					.map(entry -> convertToEmployeeDto(entry.getKey(), entry.getValue(), null))
					.collect(Collectors.toList());
	}
	
	private EmployeeDTO convertToEmployeeDto(
			String userId, 
			List<UserProject> projects,
			List<UserVacation> vacations) {
		
		EmployeeDTO dto = new EmployeeDTO();
		dto.setId(userId);
		dto.setName(userService.getUser(userId).getUsername());
		
		if (projects != null) {
			dto.setProjects(projects.stream().map(this::convertToProjectDTO).collect(Collectors.toList()));
		}
		else
			dto.setProjects(new ArrayList<EmployeeProjectDTO>());
		
		if (vacations != null) {
			dto.setVacations(vacations.stream().map(this::convertToVacationDTO).collect(Collectors.toList()));
		}
		else
			dto.setVacations(new ArrayList<EmployeeVacationDTO>());
		
		return dto;
	}
	
	private EmployeeVacationDTO convertToVacationDTO(UserVacation userVacation) {
		EmployeeVacationDTO dto = new EmployeeVacationDTO();
		dto.setId(userVacation.getId());
		dto.setStartDate(userVacation.getStartDate());
		dto.setFinishDate(userVacation.getFinishDate());
		
		return dto;
	}
	
	private EmployeeProjectDTO convertToProjectDTO(UserProject userProject) {
		EmployeeProjectDTO dto = new EmployeeProjectDTO();
		dto.setId(userProject.getProjectId());
		dto.setName(projectService.getProject(userProject.getProjectId()).getName());
		dto.setStartDate(userProject.getStartDate());
		dto.setFinishDate(userProject.getFinishDate());
		dto.setWorkload(userProject.getLoad());
		
		return dto;
	}
}
