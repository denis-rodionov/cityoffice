package com.rodionov.cityoffice;

import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.repository.DocumentRepository;

@SpringBootApplication
public class CityofficeApplication implements CommandLineRunner {
	
	private static final Logger logger = Logger.getLogger(CityofficeApplication.class);
	
	@Autowired
	private DocumentRepository documentRepository;

	public static void main(String[] args) {
		logger.fatal("Application start");
		SpringApplication.run(CityofficeApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		documentRepository.deleteAll();
		if (documentRepository.count() == 0) {
			documentRepository.save(new Document("First Document", LocalDate.of(2016, 11, 1)));
			documentRepository.save(new Document("Second Document", LocalDate.of(2016, 02, 3)));
		}
	}
}
