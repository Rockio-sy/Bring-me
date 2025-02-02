package org.bringme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.bringme.dto.PersonDTO;
import org.bringme.dto.RequestDTO;
import org.bringme.model.Person;
import org.bringme.model.Request;
import org.bringme.service.PersonService;
import org.bringme.service.RequestService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ControllerAdvice;

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

    @Operation(summary = "Get all requests for specific person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "204", description = "not content"),
            @ApiResponse(responseCode = "403", description = "forbidden, logic explained in the server and every response")

    })
    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAll(@RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> responseList = requestService.getAll(userId);
        responseMap.put("Requests", responseList);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }


    @Operation(summary = "Create new request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "409", description = "Conflict in email sending"),
            @ApiResponse(responseCode = "403", description = "Forbidden (Directions are incompatible, Item or tip not found or are not belong to the current user)")
    })
    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewRequest(@Valid @RequestHeader(value = "Authorization") String header, @RequestBody RequestDTO request) {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Check if request exists
        requestService.isExist(request.getItemId(), request.getTripId());

        // getting requester user id from token
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        request.setRequesterUserId(userId);

        RequestDTO responseRequest = requestService.saveRequest(request);

        responseMap.put("Message", "Request created successfully");
        responseMap.put("Request", responseRequest);
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }

    @Operation(summary = "Search filter for  user's sent requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "403", description = "No data")
    })
    @GetMapping("/filter/by/sent")
    public ResponseEntity<HashMap<String, Object>> getSentRequests(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> response = requestService.getSentRequests(userId);
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Search filter for  user's received requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "403", description = "No data")
    })
    @GetMapping("/filter/by/received")
    public ResponseEntity<HashMap<String, Object>> getReceivedRequests(@Valid @RequestHeader("Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> response = requestService.getReceivedRequests(userId);
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Search filter for user's requests by countries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "403", description = "No data")
    })
    @GetMapping("/filter/by/{from}/{to}")
    public ResponseEntity<HashMap<String, Object>> filterByDirections(@Valid @RequestHeader("Authorization") String header, @Valid @PathVariable(value = "from") int origin, @Valid @PathVariable(value = "to") int destination) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> response = requestService.filterByDirections(userId, origin, destination);
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Show user's specific request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "403", description = "No data")
    })
    @GetMapping("/spec/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable Long id) {
        Request request = requestService.getRequestById(id);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @Operation(summary = "Set request as approved")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "403", description = "Not found")
    })
    @PutMapping("/approve/{id}")
    public ResponseEntity<HashMap<String, Object>> approveRequests(@Valid @RequestHeader(value = "Authorization") String header, @Valid @PathVariable(value = "id") Long requestId) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));

        requestService.approveRequest(userId, requestId);

        responseMap.put("Message", "Request approved successfully.");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Search filter for  user's approved requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "403", description = "No data")
    })
    @GetMapping("/filter/approved")
    public ResponseEntity<HashMap<String, Object>> filterByApprovement(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> response = requestService.filterByApprovement(userId);
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Search filter for  user's not-approved requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "403", description = "No data")
    })
    @GetMapping("/filter/wait")
    public ResponseEntity<HashMap<String, Object>> filterByWait(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<RequestDTO> response = requestService.filterByWait(userId);
        responseMap.put("Requests", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Make contact visible", parameters = @Parameter(name = "id", description = "Person ID"),description = "After approved the request, requester can check the contact of the requested person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "403", description = "No data")
    })
    //TODO: CHECK IF USERS HAVE COMMON REQUEST
    @GetMapping("/contact/{id}")
    public ResponseEntity<HashMap<String, Object>> getUserDetails(@Valid @RequestHeader(value = "Authorization") String header, @Positive @PathVariable(value = "id") int hostId) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long guestId = jwtService.extractUserIdAsLong(header.substring(7));
        requestService.isThereCommonRequest(guestId, hostId);
        PersonDTO contacts = personService.showPersonDetails(hostId);
        responseMap.put("Contacts", contacts);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
