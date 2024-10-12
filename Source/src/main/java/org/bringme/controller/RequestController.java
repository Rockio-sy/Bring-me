package org.bringme.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.bringme.dto.RequestDTO;
import org.bringme.model.Request;
import org.bringme.service.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("bring-me/request")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

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

        RequestDTO responseRequest = requestService.saveRequest(request);

        if(responseRequest == null){
            responseMap.put("Message", "Unknown error");
            responseMap.put("Request", null);
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        responseMap.put("Message", "Request created successfully");
        responseMap.put("Request-id",responseRequest.getId());
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
