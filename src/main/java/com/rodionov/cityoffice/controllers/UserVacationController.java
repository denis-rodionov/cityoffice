package com.rodionov.cityoffice.controllers;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.rodionov.cityoffice.model.UserVacation;
import com.rodionov.cityoffice.repository.UserVacationRepository;
import com.rodionov.cityoffice.services.UserService;

@RestController
@RequestMapping("/user_vacation")
public class UserVacationController extends BaseController<UserVacation> {
	
	private static final Logger logger = Logger.getLogger(UserProjectController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	public UserVacationController(UserVacationRepository userVacationRepository) {
		super(userVacationRepository);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<UserVacation>> getUserVacations(
			Principal principal,
    		@RequestParam(value="_page", required=false) Integer page, 
    		@RequestParam(value="_perPage", required=false) Integer perPage,
    		@RequestParam(value="_sortField", required=false) String sortField,
    		@RequestParam(value="_sortDir", required=false) String sortDir,
    		@RequestParam(value="user", required=false) String userId) {
		
		logger.info("GET: getUserVacation");
		
		Pageable pageable = getPagiable(page, perPage, sortDir, sortField);
		
		Page<UserVacation> userVacations = userService.getUserVacations(userId, null, null, pageable);
		
		return new ResponseEntity<>(userVacations.getContent(), generatePaginationHeaders(userVacations, ""), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserVacation> getUserVacation(@PathVariable("id") String id) {
		logger.info("Fetching UserVacation with id " + id);
        return getEntity(id);
	}
	
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserVacation> createUserProject(@RequestBody UserVacation userVacation) {
        logger.info("Creating userVacation");  
        return createEntity(userVacation);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public UserVacation updateUserVacation(
    		Principal principal, 
    		@PathVariable("id") String id, 
    		@RequestBody UserVacation userVacation) {
    	logger.info("Updating UserVacation " + userVacation.getId());
         
    	UserVacation dbUserVacation = repository.findOne(userVacation.getId());
	         
        if (dbUserVacation == null) 
            throw new NotFoundException();
        
        if (userVacation.getStartDate() != null)
        	dbUserVacation.setStartDate(userVacation.getStartDate());
        
        if (userVacation.getFinishDate() != null)
        	dbUserVacation.setFinishDate(userVacation.getFinishDate());
        
        repository.save(dbUserVacation);
        return dbUserVacation;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<UserVacation> deleteUserVacation(Principal principal, @PathVariable("id") String id) 
    		throws Exception {
    	
    	if (!userDetailsService.isAdmin(principal))
    		throw new NotEnoughtRightsException();
    	
    	ResponseEntity<UserVacation> res =  deleteEntity(id);    	
    	logger.info("UserVacation " + res + " DELETED");    	
    	return res;
    }
}
