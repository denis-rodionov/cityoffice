package com.rodionov.cityoffice.model.serialization;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 *
 * custom JSON serializer for java.sql.Time type
 *
 */
public class CustomDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final Logger LOGGER = Logger.getLogger(CustomDateDeserializer.class);

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        
    	LOGGER.info("converting time parameter from value " + jp.getText());
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	LocalDate date = LocalDate.parse(jp.getText(), formatter);
    	
        return date;
    }

}
