package org.bringme.controller;

import jakarta.validation.Valid;
import org.bringme.dto.RateDTO;
import org.bringme.service.RateService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/bring-me/rate")
public class RateController {
    private final RateService rateService;
    private final JwtService jwtService;

    public RateController(RateService rateService, JwtService jwtService) {
        this.jwtService = jwtService;
        this.rateService = rateService;
    }

    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAllRates(int userId) {
        List<RateDTO> all = rateService.getAllRates(userId);
        HashMap<String, Object> response = new HashMap<>();
        response.put("Rates", all);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewRate(@RequestHeader(value = "Authorization") String header, @Validated @RequestBody RateDTO rate) {
        HashMap<String, Object> response = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        rateService.checkRatingAvailability(userId, rate.ratedUserId());
        RateDTO dto = rateService.createNewRate(rate);
        response.put("Rate", dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
