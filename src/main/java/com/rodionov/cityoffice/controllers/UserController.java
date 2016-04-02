package com.rodionov.cityoffice.controllers;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	@RequestMapping("/user")
	public Principal user(Principal user) {
		logger.debug("UserController.User accessed by '" + user + "'");
		
		return user;
	}
	
	@RequestMapping(value="logout", method=RequestMethod.POST)
	  public String logout(HttpSession session) {
	    session.invalidate();
	    return "redirect:/";
	  }
	
//	@RequestMapping(value="/logout", method=RequestMethod.GET)
//	public String logout(HttpServletRequest request, HttpServletResponse response) {
//		
//		Authentication auth = SecurityContextHolder.getContext()
//				.getAuthentication();
//		
//		if (auth != null) {
//			new SecurityContextLogoutHandler().logout(request, response, auth);
//		}
//		
//		return "redirect:/login";
//	}
	
	
}
