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

import com.rodionov.cityoffice.controllers.exceptions.CrossReferenceException;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.repository.DocumentRepository;
import com.rodionov.cityoffice.repository.ProjectRepository;
import com.rodionov.cityoffice.repository.UserRepository;

@RestController
public class ProjectController {
	
	private static final Logger logger = Logger.getLogger(ProjectController.class);
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	//-------------------Retrieve All Projects--------------------------------------------------------
    
    @RequestMapping(value = "/project", method = RequestMethod.GET)
    public ResponseEntity<List<Project>> listAllProjects() {
        List<Project> projects = projectRepository.findAll();
        if(projects.isEmpty()){
            return new ResponseEntity<List<Project>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
    }
    
    //-------------------Retrieve Single Project--------------------------------------------------------
    
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> getProject(@PathVariable("id") String id) {
        logger.debug("Fetching Project with id " + id);
        Project project = projectRepository.findOne(id);
        if (project == null) {
            System.out.println("Project with id " + id + " not found");
            return new ResponseEntity<Project>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }     
     
    //-------------------Create a Project--------------------------------------------------------
     
    @RequestMapping(value = "/project", method = RequestMethod.POST)
    public ResponseEntity<Project> createProject(@RequestBody Project project, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Project " + project.getName());
        Project existing = projectRepository.findByName(project.getName()).stream().findAny().orElse(null);
        
        if (existing != null) {
            System.out.println("A Project with name " + project.getName() + " already exist");
            return new ResponseEntity<Project>(HttpStatus.CONFLICT);
        }
 
        projectRepository.save(project);
 
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);
    }
 
     
    //------------------- Update a Project --------------------------------------------------------
     
    @RequestMapping(value = "/project/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Project> updateProject(@PathVariable("id") String id, @RequestBody Project project) {
        logger.info("Updating Project " + project.getName());
         
        Project currentProject = projectRepository.findOne(project.getId());
         
        if (currentProject == null) {
            System.out.println("Project with id " + id + " not found");
            return new ResponseEntity<Project>(HttpStatus.NOT_FOUND);
        }
 
        currentProject.setName(project.getName());
        currentProject.setActive(project.isActive());
        currentProject.setColorName(project.getColorName());
        
        projectRepository.save(currentProject);
        return new ResponseEntity<Project>(currentProject, HttpStatus.OK);
    }
 
    //------------------- Delete a Project --------------------------------------------------------
     
    @RequestMapping(value = "/project/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Project> deleteProject(@PathVariable("id") String id) 
    		throws Exception {
        Project project = projectRepository.findOne(id);
        if (project == null) {
            logger.info("Unable to delete. Project with id " + id + " not found");
            return new ResponseEntity<Project>(HttpStatus.NOT_FOUND);
        }
        
        int docCount = documentRepository.findByProjectId(project.getId()).size();
        if (docCount != 0) {
        	throw new CrossReferenceException();
        }
        
        int userCount = userRepository.findByProjectIdsIn(project.getId()).size();
        if (userCount != 0)
        	throw new CrossReferenceException();
        
        logger.info("Deleting Project with username " + project.getName());

        projectRepository.delete(id);
        return new ResponseEntity<Project>(HttpStatus.NO_CONTENT);
    }
 
     
    //------------------- Delete All Projects --------------------------------------------------------
/*     
    @RequestMapping(value = "/project/", method = RequestMethod.DELETE)
    public ResponseEntity<Project> deleteAllProjects() {
        System.out.println("Deleting All Projects");
 
        projectRepository.deleteAll();
        return new ResponseEntity<Project>(HttpStatus.NO_CONTENT);
    }*/
 
}
