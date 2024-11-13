package org.bringme.controller;

import jakarta.validation.Valid;
import org.bringme.dto.ReportDTO;
import org.bringme.service.ReportService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/bring-me/report")
public class ReportController {
    private final ReportService reportService;
    private final JwtService jwtService;

    public ReportController(ReportService reportService, JwtService jwtService) {
        this.reportService = reportService;
        this.jwtService = jwtService;
    }

    // TODO: Should i catch the error here after throwing it from the controller or no
    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewReport(@RequestHeader(value = "Authorization") String header,
                                                                    @Valid @RequestBody ReportDTO reportForm) {
        HashMap<String, Object> responseMap = new HashMap<>();
        if (header == null) {
            responseMap.put("Message", "Invalid token.");
            return new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
        }

        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);

        reportService.validateReportForm(reportForm, userId);

        ReportDTO responseReport = reportService.createNewReport(reportForm, userId);

        responseMap.put("Message", "Report created successfully.");
        responseMap.put("Report", responseReport);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
