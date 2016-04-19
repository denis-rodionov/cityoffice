package com.rodionov.cityoffice.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.rodionov.cityoffice.services.BackgroundJobService;

@Configuration
public class ServletInitializer implements ServletContextInitializer {

	private static final Logger logger = Logger.getLogger(ServletInitializer.class);
	
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	logger.info("== ServletContextInitializer");
		
		FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encoding-filter", new CharacterEncodingFilter());
	    encodingFilter.setInitParameter("encoding", "UTF-8");
	    encodingFilter.setInitParameter("forceEncoding", "true");
	    encodingFilter.addMappingForUrlPatterns(null, true, "/*");
	    
	    servletContext.addListener(new BackgroundJobService());
    }

}