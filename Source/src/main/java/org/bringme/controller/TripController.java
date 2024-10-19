package org.bringme.controller;

import org.bringme.dto.TripDTO;
import org.bringme.service.TripService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("bring-me/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }


    // TODO: use te token to get the user id
    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewTrip(@RequestBody TripDTO requestTrip) {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Data checking
        if ((requestTrip.getOrigin() == requestTrip.getDestination())
                || requestTrip.getOrigin() == 0 || requestTrip.getDestination() == 0
                || requestTrip.getEmptyWeight() == 0 || requestTrip.getPassengerId() == 0
                || requestTrip.getDepartureTime().isBefore(LocalDateTime.now())
                || requestTrip.getArrivalTime().isBefore(LocalDateTime.now())) {
            responseMap.put("Status", "422");
            responseMap.put("Message", "Invalid data");
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

        if(responseList.isEmpty()){
            responseMap.put("Status", "203");
            responseMap.put("Message", "Empty list");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }

        responseMap.put("Count fo trips", responseList.size());
        responseMap.put("Status", "200");
        responseMap.put("Message", "List found successfully.");
        responseMap.put("Trips", responseList);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
