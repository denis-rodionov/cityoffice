package com.rodionov.cityoffice.controllers;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rodionov.cityoffice.controllers.exceptions.AlreadyExistsException;
import com.rodionov.cityoffice.controllers.exceptions.NotEnoughtRightsException;
import com.rodionov.cityoffice.controllers.exceptions.NotFoundException;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.UserRepository;
import com.rodionov.cityoffice.services.MongoUserDetailsService;
import com.rodionov.cityoffice.services.UserService;

@RestController
public class UserController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MongoUserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/getuser", method = RequestMethod.GET)
	public Principal user(Principal user) {
		logger.info("UserController.User accessed by '" + user + "'");
		
		return user;
	}
	
	//-------------------Retrieve All Users--------------------------------------------------------
    
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers(
    		Principal principal,
    		@RequestParam(value="_page", required=false) Integer page, 
    		@RequestParam(value="_perPage", required=false) Integer perPage,
    		@RequestParam(value="_sortField", required=false) String sortField,
    		@RequestParam(value="_sortDir", required=false) String sortDir,
    		@RequestParam(value="role", required=false) String role,
    		@RequestParam(value="username", required=false) String username,
    		@RequestParam(value="email", required=false) String email,
    		@RequestParam(value="project", required=false) String projectId) {
    	
    	Pageable pageable = getPagiable(page, perPage, sortDir, sortField);
    	
    	Page<User> users = userService.getFilteredUsers(username, projectId, role, email, pageable);
    	
    	return new ResponseEntity<>(users.getContent(), generatePaginationHeaders(users, ""), HttpStatus.OK);
    }
    
    //-------------------Retrieve Single User--------------------------------------------------------
    
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable("id") String id) {
        logger.info("Fetching User with id " + id);
        
        User user = userRepository.findOne(id);
        
        if (user == null) {
            logger.info("User with id " + id + " not found");
            throw new NotFoundException();
        }
        
        return user;
    }
     
    //-------------------Create a User--------------------------------------------------------
     
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        logger.info("Creating User " + user.getUsername());
        
        User existing = userRepository.findByUsername(user.getUsername());
        User existingByEmail = userRepository.findByEmail(user.getUsername());
        
        if (existing == null || existingByEmail != null) {
            logger.info("A User with name " + user.getUsername() + " or email already exist");
            throw new AlreadyExistsException();
        }
 
        userRepository.save(user);
        
        return user;
    }
 
     
    //------------------- Update a User --------------------------------------------------------
    
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public User updateUser(Principal principal, @PathVariable("id") String id, @RequestBody User user) {
    	logger.info("Updating User " + user.getUsername());
    	
    	User currentUser = userDetailsService.getUserByPrincipal(principal);
    	
    	if (!userDetailsService.isAdmin(principal) && !currentUser.getEmail().equals(user.getEmail()))
    		throw new NotEnoughtRightsException();
         
        User dbUser = userRepository.findOne(user.getId());
         
        if (dbUser == null) 
            throw new NotFoundException();        
 
        dbUser.setUsername(user.getUsername());
        dbUser.setEmail(user.getEmail());
        dbUser.setProjectIds(user.getProjectIds());        
        dbUser.setPassword(user.getPassword());
        
        if (userDetailsService.isAdmin(principal))
        	dbUser.setRole(user.getRole());
                
        userRepository.save(dbUser);
        return dbUser;
    }
 
    //------------------- Delete a User --------------------------------------------------------
     
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(Principal principal, @PathVariable("id") String id) 
    		throws Exception {
    	
    	if (!userDetailsService.isAdmin(principal))
    		throw new NotEnoughtRightsException();
    	
        User user = userRepository.findOne(id);
        
        if (user == null) {
            logger.info("Unable to delete. User with id " + id + " not found");
            throw new NotFoundException();
        }
        
        logger.info("Deleting User with username " + user.getUsername());

        userRepository.delete(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
	
}
