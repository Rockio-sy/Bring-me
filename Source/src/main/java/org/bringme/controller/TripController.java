package org.bringme.controller;

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

    @GetMapping("/show")
    public ResponseEntity<HashMap<String, Object>> getTripById(@RequestParam @Positive Long id) {
        // multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Get trip
        TripDTO responseTrip = tripService.getById(id);
        responseMap.put("Trip", responseTrip);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);

    }

    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAllTrips() {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Get list of trips
        List<TripDTO> responseList = tripService.getAllTrips();
        responseMap.put("Trips", responseList);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/filter/by/countries")
    public ResponseEntity<HashMap<String, Object>> filterByCountries(@Valid @RequestParam("from") int origin, @Valid @RequestParam(name = "to") int destination) {
        // Multi-value map
        HashMap<String, Object> responseMap = new HashMap<>();
        List<TripDTO> response = tripService.filterByCountries(origin, destination);
        responseMap.put("Trips", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
