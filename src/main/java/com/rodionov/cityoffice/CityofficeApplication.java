package com.rodionov.cityoffice;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.repository.DocumentRepository;
import com.rodionov.cityoffice.repository.ProjectRepository;

//@SpringBootApplication
public class CityofficeApplication implements CommandLineRunner {
	
	private static final Logger logger = Logger.getLogger(CityofficeApplication.class);
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private ProjectRepository projectRepository;

	public static void main(String[] args) {
		logger.info("Application start");
		SpringApplication.run(CityofficeApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		documentRepository.deleteAll();
		projectRepository.deleteAll();
		if (documentRepository.count() == 0) {
			Project proj1 = projectRepository.save(new Project("Exerica", true, "primary"));
			Project proj2 = projectRepository.save(new Project("Lobster", true, "success"));			
			
			documentRepository.save(new Document("ТЗ", new GregorianCalendar(2016, 04, 15).getTime(), 
					DocumentStatus.NEW, proj1.getId()));
			documentRepository.save(new Document("Техническая записка", new GregorianCalendar(2016, 04, 27).getTime(), 
					DocumentStatus.NEW, proj1.getId()));
			documentRepository.save(new Document("Согласование списка документов", new GregorianCalendar(2016, 05, 2).getTime(), 
					DocumentStatus.NEW, proj2.getId()));
			documentRepository.save(new Document("Согласование типов клавиатур", 
					new GregorianCalendar(2016, 05, 15).getTime(), 
					DocumentStatus.NEW, proj2.getId()));
			documentRepository.save(new Document("Акт приёмки этапа", 
					new GregorianCalendar(2016, 05, 29).getTime(), 
					DocumentStatus.NEW, proj1.getId()));
		}
	}
}
