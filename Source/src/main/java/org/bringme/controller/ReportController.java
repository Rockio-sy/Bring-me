package org.bringme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.bringme.dto.ReportDTO;
import org.bringme.service.ReportService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ControllerAdvice;

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

    @Operation(summary = "Create new report", description = "Report someone by approved request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Token error, unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
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
    @Operation(summary = "List all reports for admin view", description = "Needs admin login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Token error, admin unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping("/a/all")
    public ResponseEntity<HashMap<String, Object>> getAllReports() {
        HashMap<String, Object> responseMap = new HashMap<>();
        List<ReportDTO> all = reportService.getAll();
        responseMap.put("Reports", all);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "List all not checked reports for admin view", description = "Needs admin login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Token error, admin unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping("/a/not-answered")
    public ResponseEntity<HashMap<String, Object>> getNotAnsweredReports() {
        HashMap<String, Object> responseMap = new HashMap<>();
        List<ReportDTO> reports = reportService.getNotAnswered();
        responseMap.put("Not answered reports", reports);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Show specific report for admin view", description = "Needs admin login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Token error, admin unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "204", description = "No content")
    })
    @GetMapping("/a/spec")
    public ResponseEntity<HashMap<String, Object>> getSpecificReport(@Valid @RequestParam(value = "id") Long id) {
        HashMap<String, Object> responseMap = new HashMap<>();
        ReportDTO dto = reportService.getSpecific(id);
        responseMap.put("Report", dto);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Answer report by admin", description = "Needs admin login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Token error, admin unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "204", description = "No content")
    })
    @PutMapping("/a/answer")
    public ResponseEntity<HashMap<String, Object>> answerReport(@Valid @RequestHeader(value = "Authorization") String header, @Positive @RequestParam("id") Long reportId, @NotBlank @RequestParam("answer") String answer) {
        Long adminId = jwtService.extractUserIdAsLong(header.substring(7));
        reportService.answerReport(adminId, reportId, answer);
        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("Message", "Report has been answered");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

}
