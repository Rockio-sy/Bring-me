package org.bringme.controller;

import org.bringme.model.Request;
import org.bringme.service.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Request>> getAll(){
        List<Request> requests = requestService.getAll();
        if(requests.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Request> create(@RequestBody Request request){
        Request newRequest = requestService.saveRequest(request);
        if(newRequest != null){
            return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/spec/{id}")
    public ResponseEntity<Request> getRequestById(@PathVariable Long id){
        Request request = requestService.getRequestById(id);
        if(request != null){
            return new ResponseEntity<>(request, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
