package org.bringme.service.impl;

import org.bringme.model.Country;
import org.bringme.service.exceptions.CustomException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CountryService {
    public List<Country> getCountries(){
        // Load the JSON file from the resources folder
        try {
            InputStream inputStream = new ClassPathResource("countries.json").getInputStream();

            // Create ObjectMapper to parse the JSON data
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert the JSON file to a list of Country objects
            return objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, Country.class));
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            throw new CustomException("Couldn't load countries", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
