package com.rodionov.cityoffice.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rodionov.cityoffice.model.NotificationSchema;
import com.rodionov.cityoffice.model.NotificationSchema;
import com.rodionov.cityoffice.repository.NotificationSchemaRepository;

@RestController
public class NotificationSchemaController {

	private static final Logger logger = Logger.getLogger(NotificationSchemaController.class);
	
	@Autowired
	private NotificationSchemaRepository notificationSchemaRepository;
	
	//-------------------Retrieve All NotificationSchema --------------------------------------------------------
    
    @RequestMapping(value = "/notification_schema", method = RequestMethod.GET)
    public ResponseEntity<List<NotificationSchema>> listAllNotificationSchemas() {
    	
    	logger.info("All notifications requested");
    	
        List<NotificationSchema> notificationSchemas = notificationSchemaRepository.findAll();
        if(notificationSchemas.isEmpty()){
            return new ResponseEntity<List<NotificationSchema>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<NotificationSchema>>(notificationSchemas, HttpStatus.OK);
    }
    
    //-------------------Retrieve Single NotificationSchema--------------------------------------------------------
    
    @RequestMapping(value = "/notification_schema/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificationSchema> getNotificationSchema(@PathVariable("id") String id) {
        System.out.println("Fetching NotificationSchema with id " + id);
        NotificationSchema notificationSchema = notificationSchemaRepository.findOne(id);
        if (notificationSchema == null) {
            System.out.println("NotificationSchema with id " + id + " not found");
            return new ResponseEntity<NotificationSchema>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<NotificationSchema>(notificationSchema, HttpStatus.OK);
    }
    
    //------------------- Update a NotificationSchema --------------------------------------------------------
    
    @RequestMapping(value = "/notification_schema/{id}", method = RequestMethod.PUT)
    public ResponseEntity<NotificationSchema> updateNotificationSchema(@PathVariable("id") String id, @RequestBody NotificationSchema notificationSchema) {
        System.out.println("Updating NotificationSchema " + id);
         
        NotificationSchema currentNotificationSchema = notificationSchemaRepository.findOne(notificationSchema.getId());
         
        if (currentNotificationSchema == null) {
            System.out.println("NotificationSchema with id " + id + " not found");
            return new ResponseEntity<NotificationSchema>(HttpStatus.NOT_FOUND);
        }
 
        currentNotificationSchema.setDescription(notificationSchema.getDescription());
        
        notificationSchemaRepository.save(currentNotificationSchema);
        return new ResponseEntity<NotificationSchema>(currentNotificationSchema, HttpStatus.OK);
    }
}
