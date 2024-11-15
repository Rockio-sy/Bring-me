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


    //TODO: Try this code after cleaning the code
    public RequestController(RequestService requestService, JwtService jwtService, PersonService personService) {
        this.requestService = requestService;
        this.jwtService = jwtService;
        this.personService = personService;
    }

    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAll(@RequestHeader(value = "Authorization") String header) {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> responseList = requestService.getAll(userId);
        responseMap.put("Requests", responseList);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }


    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewRequest(@Valid @RequestHeader(value = "Authorization") String header, @RequestBody RequestDTO request) {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Check if exists
        requestService.isExist(request.getItemId(), request.getTripId());

        // getting requester user id from token
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        request.setRequesterUserId(userId);

        RequestDTO responseRequest = requestService.saveRequest(request);

        responseMap.put("Message", "Request created successfully");
        responseMap.put("Request", responseRequest);
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }

    @GetMapping("/filter/by/sent")
    public ResponseEntity<HashMap<String, Object>> getSentRequests(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> response = requestService.getSentRequests(userId);
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/filter/by/received")
    public ResponseEntity<HashMap<String, Object>> getReceivedRequests(@Valid @RequestHeader("Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> response = requestService.getReceivedRequests(userId);
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/filter/by/{from}/{to}")
    public ResponseEntity<HashMap<String, Object>> filterByDirections(@Valid @RequestHeader("Authorization") String header, @Valid @PathVariable(value = "from") int origin, @Valid @PathVariable(value = "to") int destination) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> response = requestService.filterByDirections(userId, origin, destination);
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/spec/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable Long id) {
        Request request = requestService.getRequestById(id);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<HashMap<String, Object>> approveRequests(@Valid @RequestHeader(value = "Authorization") String header, @Valid @PathVariable(value = "id") Long requestId) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));

        requestService.approveRequest(userId, requestId);

        responseMap.put("Message", "Request approved successfully.");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/filter/approved")
    public ResponseEntity<HashMap<String, Object>> filterByApprovement(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> response = requestService.filterByApprovement(userId);
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/filter/wait")
    public ResponseEntity<HashMap<String, Object>> filterByWait(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> response = requestService.filterByWait(userId);
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/contact/{id}")
    public ResponseEntity<HashMap<String, Object>> getUserDetails(@Valid @RequestHeader(value = "Authorization") String header, @Valid @PathVariable(value = "id") int hostId) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long guestId = jwtService.extractUserIdAsLong(header.substring(7));
        requestService.isThereCommonRequest(guestId, hostId);
        PersonDTO contacts = personService.showPersonDetails(hostId);
        responseMap.put("Contacts", contacts);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
