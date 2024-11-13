package org.bringme.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.bringme.dto.ReportDTO;
import org.bringme.service.ReportService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/bring-me/report")
public class ReportController {
    private final ReportService reportService;
    private final JwtService jwtService;

    public ReportController(ReportService reportService, JwtService jwtService) {
        this.reportService = reportService;
        this.jwtService = jwtService;
    }

    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewReport(@RequestHeader(value = "Authorization") String header,
                                                                    @Valid @RequestBody ReportDTO reportForm) {

        // Extract user id
        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);
        // Check report
        reportService.validateReportForm(reportForm, userId);
        ReportDTO responseReport = reportService.createNewReport(reportForm, userId);
        // Response
        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("Message", "Report created successfully.");
        responseMap.put("Report", responseReport);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/a/all")
    public ResponseEntity<HashMap<String, Object>> getAllReports(){
        HashMap<String, Object> responseMap = new HashMap<>();
        List<ReportDTO> all = reportService.getAll();
        responseMap.put("Reports", all);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/a/not-answered")
    public ResponseEntity<HashMap<String, Object>> getNotAnsweredReports(){
        HashMap<String, Object> responseMap = new HashMap<>();
        List<ReportDTO> reports = reportService.getNotAnswered();
        responseMap.put("Not answered reports", reports);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/a/spec")
    public ResponseEntity<HashMap<String, Object>> getSpecificReport(@Valid @RequestParam(value = "id") Long id){
        HashMap<String, Object> responseMap = new HashMap<>();
        ReportDTO dto = reportService.getSpecific(id);
        responseMap.put("Report", dto);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
