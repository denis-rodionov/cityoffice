package com.rodionov.cityoffice;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class CityofficeApplication extends SpringBootServletInitializer  {
	
	private static final Logger logger = Logger.getLogger(CityofficeApplication.class);
	
//	@Autowired
//	private DocumentRepository documentRepository;
//	
//	@Autowired
//	private ProjectRepository projectRepository;

	public static void main(String[] args) {
		logger.info("Application start");
		SpringApplication.run(CityofficeApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CityofficeApplication.class);
    }
	
//	@Override
//	public void run(String... args) throws Exception {
//		
//		documentRepository.deleteAll();
//		projectRepository.deleteAll();
//		if (documentRepository.count() == 0) {
//			Project proj1 = projectRepository.save(new Project("Exerica", true, "primary"));
//			Project proj2 = projectRepository.save(new Project("Lobster", false, "success"));			
//			
//			documentRepository.save(new Document("ТЗ", 
//					LocalDate.of(2016, 04, 15), 
//					DocumentStatus.NEW, proj1.getId()));
//			documentRepository.save(new Document("Техническая записка", 
//					LocalDate.of(2016, 04, 27), 
//					DocumentStatus.NEW, proj1.getId()));
//			documentRepository.save(new Document("Согласование списка документов", 
//					LocalDate.of(2016, 05, 2), 
//					DocumentStatus.NEW, proj2.getId()));
//			documentRepository.save(new Document("Согласование типов клавиатур", 
//					LocalDate.of(2016, 05, 15), 
//					DocumentStatus.NEW, proj2.getId()));
//			documentRepository.save(new Document("Акт приёмки этапа", 
//					LocalDate.of(2016, 05, 29), 
//					DocumentStatus.NEW, proj1.getId()));
//		}
//	}
}
