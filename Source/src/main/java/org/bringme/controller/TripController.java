package org.bringme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.bringme.dto.TripDTO;
import org.bringme.service.TripService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("bring-me/trips")
public class TripController {

    private final TripService tripService;
    private final JwtService jwtService;

    public TripController(TripService tripService, JwtService jwtService) {
        this.jwtService = jwtService;
        this.tripService = tripService;
    }

    @Operation(summary = "Create new trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "403", description = "Already exists.")

    })
    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewTrip(@RequestHeader(value = "Authorization") String header, @RequestBody TripDTO requestTrip) {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        Long passengerId = jwtService.extractUserIdAsLong(header.substring(7));
        requestTrip.setPassengerId(passengerId);

        tripService.validateTrip(requestTrip);

        // Saving trip and return trip id
        TripDTO responseTrip = tripService.saveTrip(requestTrip);

        responseMap.put("Message", "Trip created successfully.");
        responseMap.put("Trip", responseTrip);
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }

    @Operation(summary = "Show specific trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "404", description = "Not found")

    })
    @GetMapping("/show")
    public ResponseEntity<HashMap<String, Object>> getTripById(@RequestParam @Positive Long id) {
        // multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Get trip
        TripDTO responseTrip = tripService.getById(id);
        responseMap.put("Trip", responseTrip);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);

    }

    @Operation(summary = "Show all trips")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
            @ApiResponse(responseCode = "404", description = "No data")

    })
    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAllTrips() {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Get list of trips
        List<TripDTO> responseList = tripService.getAllTrips();
        responseMap.put("Trips", responseList);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }


    @Operation(summary = "Search filter for trips by countries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")

    })
    @GetMapping("/filter/by/countries")
    public ResponseEntity<HashMap<String, Object>> filterByCountries(@Valid @RequestParam("from") int origin, @Valid @RequestParam(name = "to") int destination) {
        // Multi-value map
        HashMap<String, Object> responseMap = new HashMap<>();
        List<TripDTO> response = tripService.filterByCountries(origin, destination);
        responseMap.put("Trips", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
