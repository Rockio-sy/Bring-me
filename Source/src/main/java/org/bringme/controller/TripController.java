package org.bringme.controller;

import org.bringme.dto.TripDTO;
import org.bringme.service.TripService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("bring-me/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService){
        this.tripService = tripService;
    }

    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewTrip(@RequestBody TripDTO requestTrip)
    {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Data checking
        if((requestTrip.getOrigin() == requestTrip.getDestination())
                || requestTrip.getOrigin() == 0 || requestTrip.getDestination() == 0
                || requestTrip.getEmptyWeight() == 0 || requestTrip.getPassengerId() == 0
                || requestTrip.getDepartureTime().isBefore(LocalDateTime.now())
                || requestTrip.getArrivalTime().isBefore(LocalDateTime.now()))
        {
            responseMap.put("Status", "422");
            responseMap.put("Message", "Invalid data");
            return new ResponseEntity<>(responseMap, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Saving trip and return trip id
        TripDTO responseTrip = tripService.saveTrip(requestTrip);
        if(responseTrip == null){
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
}
