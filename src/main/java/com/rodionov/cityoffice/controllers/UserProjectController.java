package com.rodionov.cityoffice.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.model.UserProject;
import com.rodionov.cityoffice.services.UserService;

@RestController
@RequestMapping("/user_project")
public class UserProjectController extends BaseController {

	private static final Logger logger = Logger.getLogger(UserProjectController.class);
	
	@Autowired UserService userService;
	
	//-------------------Retrieve All UsersProjects --------------------------------------------------------
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserProject>> listAllUserProjects(
    		Principal principal,
    		@RequestParam(value="_page", required=false) Integer page, 
    		@RequestParam(value="_perPage", required=false) Integer perPage,
    		@RequestParam(value="_sortField", required=false) String sortField,
    		@RequestParam(value="_sortDir", required=false) String sortDir,
    		@RequestParam(value="project", required=false) String projectId,
    		@RequestParam(value="user", required=false) String username,
    		@RequestParam(value="before", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startBefore,
    		@RequestParam(value="after", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finishAfter) {
    	
    	logger.info("Fetching user's projects. Params: ");
    	
    	Pageable pageable = getPagiable(page, perPage, sortDir, sortField);
    	
    	List<String> projects = getAvailableProjects(principal);
    	projects = projectId == null ? projects : Arrays.asList(projectId);
    	
    	Page<UserProject> upList = userService.getUserProjects(projects, username, startBefore, finishAfter, pageable);
    	
        return new ResponseEntity<>(upList.getContent(), generatePaginationHeaders(upList, ""), HttpStatus.OK);
    }
    
    //-------------------Retrieve Single UserProject --------------------------------------------------------
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable("id") String id) {
        logger.info("Fetching UserProject with id " + id);

        return null;
    }
}
