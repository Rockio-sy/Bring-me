package org.bringme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bringme.model.Country;
import org.bringme.service.impl.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @Operation(summary = "Get list of countries")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Countries listed successfully"
            , content = @Content(mediaType = "application/json"))
            , @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/api/countries")
    public List<Country> getCountries() {
        return countryService.getCountries();
    }
}
