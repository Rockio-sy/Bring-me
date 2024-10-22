package org.bringme.controller;

import jakarta.validation.Valid;
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

    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewTrip(@RequestHeader(value = "Authorization") String header, @RequestBody TripDTO requestTrip) {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Validate token
        if (header == null || !header.startsWith("Bearer ")) {
            responseMap.put("Message", "Header is null");
            return new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
        }

        String token = header.substring(7);
        Long passengerId = jwtService.extractUserIdAsLong(token);
        requestTrip.setPassengerId(passengerId);

        // Data checking
        if (
                (requestTrip.getOrigin() == requestTrip.getDestination())
                        || requestTrip.getOrigin() == 0 || requestTrip.getDestination() == 0
                        || requestTrip.getEmptyWeight() == 0 || requestTrip.getPassengerId() == 0
        ) {
            responseMap.put("Status", "422");
            responseMap.put("Message", "Invalid data");
            return new ResponseEntity<>(responseMap, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (!tripService.checkTripTime(requestTrip.getDepartureTime(), requestTrip.getArrivalTime())) {
            responseMap.put("Status", "422");
            responseMap.put("Message", "Invalid time data");
            return new ResponseEntity<>(responseMap, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Saving trip and return trip id
        TripDTO responseTrip = tripService.saveTrip(requestTrip);
        if (responseTrip == null) {
            responseMap.put("Status", "500");
            responseMap.put("Message", "Unknown error");
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        responseMap.put("Status", "201");
        responseMap.put("Message", "Trip created successfully.");
        responseMap.put("Trip-id", responseTrip.getId());
        responseMap.put("Trip", responseTrip);
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<HashMap<String, Object>> getTripById(@PathVariable Long id) {
        // multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        if (id == 0) {
            responseMap.put("Status", "409");
            responseMap.put("Message", "Invalid data (id)");
            return new ResponseEntity<>(responseMap, HttpStatus.CONFLICT);
        }

        // Get trip
        TripDTO responseTrip = tripService.getById(id);

        // Check if response is null
        if (responseTrip == null) {
            responseMap.put("Status", "204");
            responseMap.put("Message", "Unknown error");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }

        responseMap.put("Status", "200");
        responseMap.put("Message", "Trip found successfully.");
        responseMap.put("Trip", responseTrip);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);

    }

    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAllTrips() {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Get list of trips
        List<TripDTO> responseList = tripService.getAllTrips();

        if (responseList.isEmpty()) {
            responseMap.put("Status", "203");
            responseMap.put("Message", "Empty list");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }

        responseMap.put("Status", "200");
        responseMap.put("Message", "List found successfully.");
        responseMap.put("Trips", responseList);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/filter/{from}/{to}")
    public ResponseEntity<HashMap<String, Object>> filterByCountries(@Valid @PathVariable("from") int origin, @Valid @PathVariable(name = "to") int destination) {
        // Multi-value map
        HashMap<String, Object> responseMap = new HashMap<>();

        List<TripDTO> response = tripService.filterByCountries(origin, destination);
        if (response.isEmpty()) {
            responseMap.put("Message", "No content.");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }

        responseMap.put("Message", "Data returned successfully.");
        responseMap.put("Trips", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
