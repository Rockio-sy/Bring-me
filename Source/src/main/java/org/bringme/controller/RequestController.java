package org.bringme.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.bringme.dto.RequestDTO;
import org.bringme.model.Request;
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

    public RequestController(RequestService requestService, JwtService jwtService) {
        this.requestService = requestService;
        this.jwtService = jwtService;
    }

    // TODO: use token to get the user id
    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAll() {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();



        List<RequestDTO> responseList = requestService.getAll();
        if (responseList.isEmpty()) {
            responseMap.put("Message", "No content");
            responseMap.put("Requests", null);
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }
        responseMap.put("Message", "List returned successfully");
        responseMap.put("Requests", responseList);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }


    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewRequest(@Valid @RequestBody RequestDTO request) {
        // Multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Checking input
        if ((request.getRequestedUserId().equals(request.getRequesterUserId()) ||
                request.getRequesterUserId() < 1 || request.getRequestedUserId() < 1
                || request.getOrigin().equals(request.getDestination())
                || request.getDestination() < 1 || request.getOrigin() < 1)) {
            responseMap.put("Message", "Invalid data");
            responseMap.put("Request", null);
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        RequestDTO responseRequest = requestService.saveRequest(request);

        if (responseRequest == null) {
            responseMap.put("Message", "Unknown error");
            responseMap.put("Request", null);
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        responseMap.put("Message", "Request created successfully");
        responseMap.put("Request-id", responseRequest.getId());
        responseMap.put("Request", responseRequest);
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
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
}
