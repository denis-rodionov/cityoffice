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
import org.springframework.web.util.UriComponentsBuilder;

import com.rodionov.cityoffice.model.Notification;
import com.rodionov.cityoffice.repository.DocumentRepository;
import com.rodionov.cityoffice.repository.NotificationRepository;

@RestController
public class NotificationController {
	
	private static final Logger logger = Logger.getLogger(NotificationController.class);
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	//-------------------Retrieve All Notifications--------------------------------------------------------
    
    @RequestMapping(value = "/notification", method = RequestMethod.GET)
    public ResponseEntity<List<Notification>> listAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        if(notifications.isEmpty()){
            return new ResponseEntity<List<Notification>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Notification>>(notifications, HttpStatus.OK);
    }
    
    //-------------------Retrieve Single Notification--------------------------------------------------------
    
    @RequestMapping(value = "/notification/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notification> getNotification(@PathVariable("id") String id) {
        System.out.println("Fetching Notification with id " + id);
        Notification notification = notificationRepository.findOne(id);
        if (notification == null) {
            System.out.println("Notification with id " + id + " not found");
            return new ResponseEntity<Notification>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Notification>(notification, HttpStatus.OK);
    }     
     
    //-------------------Create a Notification--------------------------------------------------------
     
    @RequestMapping(value = "/notification", method = RequestMethod.POST)
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Notification " + notification.getName());
        Notification existing = notificationRepository.findByName(notification.getName()).stream().findAny().orElse(null);
        
        if (existing != null) {
            System.out.println("A Notification with name " + notification.getName() + " already exist");
            return new ResponseEntity<Notification>(HttpStatus.CONFLICT);
        }
 
        notificationRepository.save(notification);
 
        //HttpHeaders headers = new HttpHeaders();
        //headers.setLocation(ucBuilder.path("/notification/{id}").buildAndExpand(notification.getId()).toUri());
        //return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        return new ResponseEntity<Notification>(notification, HttpStatus.CREATED);
    }
 
     
    //------------------- Update a Notification --------------------------------------------------------
     
    @RequestMapping(value = "/notification/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Notification> updateNotification(@PathVariable("id") String id, @RequestBody Notification notification) {
        System.out.println("Updating Notification " + id);
         
        Notification currentNotification = notificationRepository.findOne(notification.getId());
         
        if (currentNotification == null) {
            System.out.println("Notification with id " + id + " not found");
            return new ResponseEntity<Notification>(HttpStatus.NOT_FOUND);
        }
 
        currentNotification.setName(notification.getName());
        currentNotification.setDaysBefore(notification.getDaysBefore());
        
        notificationRepository.save(currentNotification);
        return new ResponseEntity<Notification>(currentNotification, HttpStatus.OK);
    }
 
    //------------------- Delete a Notification --------------------------------------------------------
     
    @RequestMapping(value = "/notification/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Notification> deleteNotification(@PathVariable("id") String id) 
    		throws Exception {
        System.out.println("Fetching & Deleting Notification with id " + id);
 
        Notification notification = notificationRepository.findOne(id);
        if (notification == null) {
            logger.info("Unable to delete. Notification with id " + id + " not found");
            return new ResponseEntity<Notification>(HttpStatus.NOT_FOUND);
        }

        notificationRepository.delete(id);
        return new ResponseEntity<Notification>(HttpStatus.NO_CONTENT);
    }
 
}
