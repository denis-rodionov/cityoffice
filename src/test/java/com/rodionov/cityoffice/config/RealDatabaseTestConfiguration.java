package com.rodionov.cityoffice.config;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories(value="com.rodionov.cityoffice.repository")
@ComponentScan("com.rodionov.cityoffice.services")
@PropertySource("classpath:application.properties")
public class RealDatabaseTestConfiguration extends AbstractMongoConfiguration {
	
	public static final String TEST_DATABASE_NAME = "cityoffice-test";

	@Override
    protected String getDatabaseName() {
		return TEST_DATABASE_NAME;
    }

	@Override
    public Mongo mongo() throws Exception {
        /**
         *
         * this is for a single db
         */

        return new MongoClient();


        /**
         *
         * This is for a relset of db's
         */

//        return new Mongo(new ArrayList<ServerAddress>() {
//        	{ 
//	        	add(new ServerAddress("127.0.0.1", 27017)); 
//	        	add(new ServerAddress("127.0.0.1", 27027)); 
//	        	add(new ServerAddress("127.0.0.1", 27037)); 
//	        }
//       	});
    }

    @Override
    protected String getMappingBasePackage() {
        return "com.rodionov.cityoffice";
    }
    
    @Bean public MailSender getMailSender() {
    	return mock(MailSender.class);
    }
    
    @Bean public SimpleMailMessage getMailMessage() {
    	return mock(SimpleMailMessage.class);
    }
}
