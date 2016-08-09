package com.rodionov.cityoffice.config;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rodionov.cityoffice.services.mail.DeadlineMailTemplate;
import com.rodionov.cityoffice.services.mail.EnGeneralTemplate;
import com.rodionov.cityoffice.services.mail.RuGeneralTemplate;

@Configuration 
public class MailConfig {
	
	Logger log = Logger.getLogger(MailConfig.class);

    @Value("${email.host}")
    private String host;

    @Value("${email.from}")
    private String from;
    
    @Value("${email.password}")
    private String password;
    
    @Bean 
    public MimeMessage getSimpleMailMessage() {
    	try {
    		Session session = Session.getInstance(getMailProperties(), getAuth());        	
        	MimeMessage message = new MimeMessage(session);        	
			message.setFrom(new InternetAddress(from, "CityOffice"));
			
			return message;
		} catch (UnsupportedEncodingException | MessagingException e) {
			log.error("cannot init mail service", e);
			return null;
		}
    }
    
    @Bean
    public DeadlineMailTemplate getDeadlineMailTemplate() {
    	return new RuGeneralTemplate();
    }
    
    private Properties getMailProperties() {
    	Properties props = new Properties();
	  	  //props.setProperty("mail.smtp.host", "smtp.gmail.com");
	  	  //props.setProperty("mail.smtp.auth", "true");
	  	  //props.setProperty("mail.smtp.port", "" + 587);
	  	  //props.setProperty("mail.smtp.starttls.enable", "true");
    	
    	props.put("mail.smtp.host", host);
    	//props.put("mail.smtp.port", port);
    	props.put("mail.smtp.auth", "true");
    	props.put("mail.smtp.ssl.enable", "true");
	  	  
	  	return props;
    }
    
    private Authenticator getAuth() {
    	Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };
        
        return auth;
    }
}