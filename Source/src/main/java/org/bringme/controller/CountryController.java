package org.bringme.controller;

import org.bringme.model.Country;
import org.bringme.service.impl.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    // Define the endpoint to get the list of countries
    @GetMapping("/api/countries")
    public List<Country> getCountries() {
        return countryService.getCountries();
    }
}
