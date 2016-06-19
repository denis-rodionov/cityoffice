package com.rodionov.cityoffice.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.model.UserProject;
import com.rodionov.cityoffice.services.UserService;

@RestController
@RequestMapping("/user_projects")
public class UserProjectController {

	private static final Logger logger = Logger.getLogger(UserProjectController.class);
	
	@Autowired UserService userService;
	
	//-------------------Retrieve All UsersProjects --------------------------------------------------------
    
    @RequestMapping(method = RequestMethod.GET)
    public List<UserProject> listAllUsers() {
    	logger.info("Fetching all users projects");
    	
    	List<UserProject> usersProjects = userService.getAllUsersProjects();
    	
        return usersProjects;
    }
    
    //-------------------Retrieve Single UserProject --------------------------------------------------------
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable("id") String id) {
        logger.info("Fetching UserProject with id " + id);

        return null;
    }
}
