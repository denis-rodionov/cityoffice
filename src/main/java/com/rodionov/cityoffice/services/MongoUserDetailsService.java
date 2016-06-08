package com.rodionov.cityoffice.services;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.MUserDetails;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.UserRepository;

// Exmaple: http://stackoverflow.com/questions/29606290/authentication-with-spring-security-spring-data-mongodb

@Service
public class MongoUserDetailsService implements UserDetailsService {
	
	private static final Logger logger = Logger.getLogger(MongoUserDetailsService.class);
	
	@Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = userRepository.findByEmail(username);
        
        if(user == null){
            throw new UsernameNotFoundException(username);
        }else{
            UserDetails details = new MUserDetails(user);
            return details;
        }
    }
    
    public User getUserByPrincipal(Principal principal) {
    	return userRepository.findByEmail(principal.getName());
    }
    
    public boolean isAdmin(Principal principal) {
    	User user = getUserByPrincipal(principal);
    	logger.info("user: " + user);
    	return user.getRole().equals(MUserDetails.ADMIN_ROLE);
    }
}
