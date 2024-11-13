package org.bringme.controller;

import jakarta.validation.Valid;
import org.bringme.dto.PersonDTO;
import org.bringme.dto.RequestDTO;
import org.bringme.model.Request;
import org.bringme.service.PersonService;
import org.bringme.service.RequestService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("bring-me/request")
public class RequestController {

    private final RequestService requestService;
    private final JwtService jwtService;
    private final PersonService personService;

    public RequestController(RequestService requestService, JwtService jwtService, PersonService personService) {
        this.requestService = requestService;
        this.jwtService = jwtService;
        this.personService = personService;
    }

    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAll(@RequestHeader(value = "Authorization") String header) {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);

        List<RequestDTO> responseList = requestService.getAll(userId);
        if (responseList == null) {
            responseMap.put("Message", "No content");
            responseMap.put("Requests", null);
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }
        responseMap.put("Message", "List returned successfully");
        responseMap.put("Requests", responseList);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }


    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewRequest(@Valid @RequestHeader(value = "Authorization") String header, @RequestBody RequestDTO request) {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Check if exists
        Long id = requestService.isExists(request.getItemId(), request.getTripId());
        if (id != null) {
            responseMap.put("Message", "Request already existed");
            responseMap.put("id", id);
            return new ResponseEntity<>(responseMap, HttpStatus.CONFLICT);
        }

        // getting requester user id from token
        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);
        request.setRequesterUserId(userId);

        RequestDTO responseRequest = requestService.saveRequest(request);

        // Trip or item not found
        if (responseRequest == null) {
            responseMap.put("Message", "Invalid trip or item");
            responseMap.put("Request", null);
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        responseMap.put("Message", "Request created successfully");
        responseMap.put("Request-id", responseRequest.getId());
        responseMap.put("Request", responseRequest);
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }

    @GetMapping("/filter/by/sent")
    public ResponseEntity<HashMap<String, Object>> getSentRequests(@Valid @RequestHeader(value = "Authorization") String header) {
        // Multi-value map
        HashMap<String, Object> responseMap = new HashMap<>();

        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);

        List<RequestDTO> response = requestService.getSentRequests(userId);
        if (response.isEmpty()) {
            responseMap.put("Message", "No Content");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }
        responseMap.put("Message", "Data returned successfully.");
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/filter/by/received")
    public ResponseEntity<HashMap<String, Object>> getReceivedRequests(@Valid @RequestHeader("Authorization") String header) {
        // Multi-value map
        HashMap<String, Object> responseMap = new HashMap<>();


        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);

        List<RequestDTO> response = requestService.getReceivedRequests(userId);
        if (response.isEmpty()) {
            responseMap.put("Message", "No Content");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }
        responseMap.put("Message", "Data returned successfully.");
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/filter/by/{from}/{to}")
    public ResponseEntity<HashMap<String, Object>> filterByDirections(@Valid @RequestHeader("Authorization") String header, @Valid @PathVariable(value = "from") int origin, @Valid @PathVariable(value = "to") int destination) {

        // Multi-value map
        HashMap<String, Object> responseMap = new HashMap<>();


        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);

        List<RequestDTO> response = requestService.filterByDirections(userId, origin, destination);

        if (response.isEmpty()) {
            responseMap.put("Message", "No Content");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }
        responseMap.put("Message", "Data returned successfully.");
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/spec/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable Long id) {
        Request request = requestService.getRequestById(id);
        if (request != null) {
            return new ResponseEntity<>(request, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<HashMap<String, Object>> approveRequests(@Valid @RequestHeader(value = "Authorization") String header, @Valid @PathVariable(value = "id") Long requestId) {
        // Multi-value map
        HashMap<String, Object> responseMap = new HashMap<>();


        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);

        boolean check = requestService.approveRequest(userId, requestId);
        if (!check) {
            responseMap.put("Message", "Internal error");
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        responseMap.put("Message", "Request approved successfully.");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/filter/approved")
    public ResponseEntity<HashMap<String, Object>> filterByApprovement(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();


        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);

        List<RequestDTO> response = requestService.filterByApprovement(userId);
        if (response.isEmpty()) {
            responseMap.put("Message", "No Content");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }
        responseMap.put("Message", "Data returned successfully.");
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/filter/wait")
    public ResponseEntity<HashMap<String, Object>> filterByWait(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();


        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);

        List<RequestDTO> response = requestService.filterByWait(userId);
        if (response.isEmpty()) {
            responseMap.put("Message", "No Content");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }
        responseMap.put("Message", "Data returned successfully.");
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/contact/{id}")
    public ResponseEntity<HashMap<String, Object>> getUserDetails(@Valid @RequestHeader(value = "Authorization") String header, @Valid @PathVariable(value = "id") int hostId) {
        HashMap<String, Object> responseMap = new HashMap<>();


        String token = header.substring(7);
        Long guestId = jwtService.extractUserIdAsLong(token);

        boolean check = requestService.isThereCommonRequest(guestId, hostId);
        if (!check) {
            responseMap.put("Message", "No common working requests");
            return new ResponseEntity<>(responseMap, HttpStatus.FORBIDDEN);
        }

        PersonDTO contacts = personService.showPersonDetails(hostId);
        responseMap.put("Message", "Data successfully returned");
        responseMap.put("Contacts", contacts);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
