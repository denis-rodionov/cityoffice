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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rodionov.cityoffice.controllers.exceptions.NotEnoughtRightsException;
import com.rodionov.cityoffice.controllers.exceptions.NotFoundException;
import com.rodionov.cityoffice.model.UserProject;
import com.rodionov.cityoffice.repository.UserProjectRepository;
import com.rodionov.cityoffice.services.UserService;

@RestController
@RequestMapping("/user_project")
public class UserProjectController extends BaseController<UserProject> {

	private static final Logger logger = Logger.getLogger(UserProjectController.class);
	
	@Autowired 
	UserService userService;
	
	@Autowired
	public UserProjectController(UserProjectRepository userRepository) {
		super(userRepository);
	}
	
	//-------------------Retrieve All UsersProjects --------------------------------------------------------
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserProject>> listAllUserProjects(
    		Principal principal,
    		@RequestParam(value="_page", required=false) Integer page, 
    		@RequestParam(value="_perPage", required=false) Integer perPage,
    		@RequestParam(value="_sortField", required=false) String sortField,
    		@RequestParam(value="_sortDir", required=false) String sortDir,
    		@RequestParam(value="project", required=false) String projectId,
    		@RequestParam(value="user", required=false) String userId,
    		@RequestParam(value="before", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startBefore,
    		@RequestParam(value="after", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finishAfter) {
    	
    	logger.info("Fetching user's projects. Params: ");
    	
    	Pageable pageable = getPagiable(page, perPage, sortDir, sortField);
    	
    	List<String> projects = getAvailableProjects(principal);
    	projects = projectId == null ? projects : Arrays.asList(projectId);
    	
    	Page<UserProject> upList = userService.getUserProjects(projects, userId, startBefore, finishAfter, pageable);
    	
        return new ResponseEntity<>(upList.getContent(), generatePaginationHeaders(upList, ""), HttpStatus.OK);
    }
    
    //-------------------Retrieve Single UserProject --------------------------------------------------------    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserProject> getUserProject(@PathVariable("id") String id) {
        logger.info("Fetching UserProject with id " + id);
        return getEntity(id);
    }
    
    
    //-------------------Create a Single UserProject --------------------------------------------------------
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserProject> createUserProject(@RequestBody UserProject userProject) {
        logger.info("Creating UserProject");  
        return createEntity(userProject);
    }
    
    //------------------- Update a UserProject --------------------------------------------------------    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public UserProject updateUserProject(Principal principal, @PathVariable("id") String id, @RequestBody UserProject userProject) {
    	logger.info("Updating UserProject " + userProject.getId());
         
        UserProject dbUserProject = repository.findOne(userProject.getId());
         
        if (dbUserProject == null) 
            throw new NotFoundException();
 
        dbUserProject.setUserId(userProject.getUserId());
        dbUserProject.setProjectId(userProject.getProjectId());
        dbUserProject.setLoad(userProject.getLoad());
        
        if (userProject.getStartDate() != null)
        	dbUserProject.setStartDate(userProject.getStartDate());
        
        if (userProject.getFinishDate() != null)
        	dbUserProject.setFinishDate(userProject.getFinishDate());
        
        repository.save(dbUserProject);
        return dbUserProject;
    }
    
    //------------------- Delete a UserProject --------------------------------------------------------     
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<UserProject> deleteUserProject(Principal principal, @PathVariable("id") String id) 
    		throws Exception {
    	
    	if (!userDetailsService.isAdmin(principal))
    		throw new NotEnoughtRightsException();
    	
    	ResponseEntity<UserProject> res =  deleteEntity(id);    	
    	logger.info("UserProject " + res + " DELETED");    	
    	return res;
    }
}
