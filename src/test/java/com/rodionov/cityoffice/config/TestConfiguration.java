package com.rodionov.cityoffice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import com.rodionov.cityoffice.repository.ProjectRepository;

@Configuration
@EnableMongoRepositories
@ComponentScan(basePackageClasses = {ProjectRepository.class})
@PropertySource("classpath:application.properties")
public class TestConfiguration extends AbstractMongoConfiguration  {
	
//	@Bean
//	public ProjectRepository projectRepository() {
//		return new ProjectRepositoryMock();
//	}
	
	@Override
	protected String getDatabaseName() {
		return "cityoffice-db";
	}

	@Override
	public Mongo mongo() {
		return null;	//return new Fongo("citoffice-test").getMongo();
	}

	@Override
	protected String getMappingBasePackage() {
		return "com.rodionov.cityoffice.repository";
	}
}
