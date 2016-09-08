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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rodionov.cityoffice.controllers.exceptions.AlreadyExistsException;
import com.rodionov.cityoffice.controllers.exceptions.CrossReferenceException;
import com.rodionov.cityoffice.model.NotificationSchema;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.DocumentRepository;
import com.rodionov.cityoffice.repository.NotificationSchemaRepository;

@RestController
public class NotificationSchemaController extends BaseController<NotificationSchema> {

	private static final Logger logger = Logger.getLogger(NotificationSchemaController.class);
	
	@Autowired
	public NotificationSchemaController(NotificationSchemaRepository notificationSchemaRepository) {
		super(notificationSchemaRepository);
	}
	
	@Autowired
	private DocumentRepository documentRepository;
	
	//-------------------Retrieve All NotificationSchema --------------------------------------------------------
    
    @RequestMapping(value = "/notification_schema", method = RequestMethod.GET)
    public ResponseEntity<List<NotificationSchema>> listAllNotificationSchemas() {
    	
    	logger.info("All notifications requested");
    	
        List<NotificationSchema> notificationSchemas = repository.findAll();
        if(notificationSchemas.isEmpty()){
            return new ResponseEntity<List<NotificationSchema>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<NotificationSchema>>(notificationSchemas, HttpStatus.OK);
    }
    
    //-------------------Retrieve Single NotificationSchema--------------------------------------------------------
    
    @RequestMapping(value = "/notification_schema/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificationSchema> getNotificationSchema(@PathVariable("id") String id) {
        System.out.println("Fetching NotificationSchema with id " + id);
        NotificationSchema notificationSchema = repository.findOne(id);
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
         
        NotificationSchema currentNotificationSchema = repository.findOne(notificationSchema.getId());
        
        if (currentNotificationSchema == null) {
            System.out.println("NotificationSchema with id " + id + " not found");
            return new ResponseEntity<NotificationSchema>(HttpStatus.NOT_FOUND);
        }
 
        currentNotificationSchema.setName(notificationSchema.getName());
        currentNotificationSchema.setNotifications(notificationSchema.getNotifications());
        
        repository.save(currentNotificationSchema);
        return new ResponseEntity<NotificationSchema>(currentNotificationSchema, HttpStatus.OK);
    }
    
    //-------------------Create a NotificationSchema--------------------------------------------------------
    
    @RequestMapping(value = "/notification_schema", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationSchema createNotificationSchema(
    		@RequestBody NotificationSchema notificationSchema, 
    		UriComponentsBuilder ucBuilder) {
    	
        System.out.println("Creating NotificationSchema " + notificationSchema.getName());
        
        NotificationSchema existing = ((NotificationSchemaRepository)repository)
        		.findByName(notificationSchema.getName()).stream().findAny().orElse(null);
        
        if (existing != null) {
            System.out.println("A NotificationSchema with name " + notificationSchema.getName() + " already exist");
            throw new AlreadyExistsException();
        }
 
        repository.save(notificationSchema);
 
        //HttpHeaders headers = new HttpHeaders();
        //headers.setLocation(ucBuilder.path("/notificationSchema/{id}").buildAndExpand(notificationSchema.getId()).toUri());
        //return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        return notificationSchema;
    }
    
    //------------------- Delete a NotificationSchema --------------------------------------------------------
    
    @RequestMapping(value = "/notification_schema/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<NotificationSchema> deleteNotificationSchema(@PathVariable("id") String id) 
    		throws Exception {
        System.out.println("Fetching & Deleting NotificationSchema with id " + id);
 
        NotificationSchema notificationSchema = repository.findOne(id);
        if (notificationSchema == null) {
            logger.info("Unable to delete. NotificationSchema with id " + id + " not found");
            return new ResponseEntity<NotificationSchema>(HttpStatus.NOT_FOUND);
        }
        
        int refCount = documentRepository.findByNotificationSchemaId(notificationSchema.getId()).size();
        if (refCount != 0)
        	throw new CrossReferenceException();

        ResponseEntity<NotificationSchema> res =  deleteEntity(id);    	
    	logger.info("NotificationSchema " + res + " DELETED");    	
    	return res;
    }
}
