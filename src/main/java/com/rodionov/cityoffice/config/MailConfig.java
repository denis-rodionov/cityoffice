package com.rodionov.cityoffice.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.rodionov.cityoffice.services.mail.DeadlineMailTemplate;
import com.rodionov.cityoffice.services.mail.EnGeneralTemplate;

@Configuration 
public class MailConfig {

    @Value("${email.host}")
    private String host;

    @Value("${email.from}")
    private String from;
    
    @Value("${email.password}")
    private String password;

    @Bean
    public MailSender javaMailService() {
    	Properties props = new Properties();
    	  //props.setProperty("mail.smtp.host", "smtp.gmail.com");
    	  //props.setProperty("mail.smtp.auth", "true");
    	  //props.setProperty("mail.smtp.port", "" + 587);
    	  //props.setProperty("mail.smtp.starttls.enable", "true");
    	  props.put("mail.smtp.ssl.enable", "true");
    	
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(from);
        javaMailSender.setPassword(password);
        javaMailSender.setJavaMailProperties(props);
        return javaMailSender;
    }
    
    @Bean 
    public SimpleMailMessage getSimpleMailMessage() {
    	SimpleMailMessage message = new SimpleMailMessage();
    	message.setFrom(from);;
    	
    	return message;
    }
    
    @Bean
    public DeadlineMailTemplate getDeadlineMailTemplate() {
    	return new EnGeneralTemplate();
    }
}