package com.rodionov.cityoffice.services;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.NotificationSchema;
import com.rodionov.cityoffice.model.helpers.NotificationHelper;
import com.rodionov.cityoffice.repository.NotificationSchemaRepository;

@Service
public class DatabaseLoader {
	
	private static final Logger logger = Logger.getLogger(DatabaseLoader.class);

    private final NotificationSchemaRepository notificationSchemaRepository;

    @Autowired
    public DatabaseLoader(NotificationSchemaRepository notificationSchemaRepository) {
        this.notificationSchemaRepository = notificationSchemaRepository;
    }

    @PostConstruct
    private void initDatabase() {
    	logger.info("initDatabase");
    	
    	if (notificationSchemaRepository.count() == 0) {
    		logger.info("Population database...");
    		
    		List<NotificationSchema> schemas = NotificationHelper.createNotificationSchemes();
    		
    		for (NotificationSchema schema : schemas) {
    			notificationSchemaRepository.save(schema);
    		}
    	}

    }
}