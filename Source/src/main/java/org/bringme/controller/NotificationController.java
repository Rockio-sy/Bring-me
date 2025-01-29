package org.bringme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.bringme.dto.NotificationDTO;
import org.bringme.service.NotificationService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/bring-me/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final JwtService jwtService;

    public NotificationController(NotificationService notificationService, JwtService jwtService) {
        this.notificationService = notificationService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "List all notifications for specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized token, User not found")
    })
    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAllNotifications(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        List<NotificationDTO> all = notificationService.getAll(userId.intValue());
        responseMap.put("Notifications", all);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "List all  unread notifications for specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized token, User not found")
    })
    @GetMapping("/not-marked")
    public ResponseEntity<HashMap<String, Object>> getNotMarkedNotifications(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();

        Long userId = jwtService.extractUserIdAsLong(header.substring(7));

        List<NotificationDTO> notMarked = notificationService.getNotMarked(userId.intValue());

        responseMap.put("Not-marked", notMarked);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Mark one notification as read for specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized token, User not found")
    })
    @PatchMapping("/mark-one")
    public ResponseEntity<HashMap<String, Object>> markOneAsRead(@Valid @RequestHeader(value = "Authorization") String header, @RequestParam(value = "id") Long id) {
        HashMap<String, Object> responseMap = new HashMap<>();
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        notificationService.markOneAsRead(userId.intValue(), id);
        responseMap.put("Message", "marked as read successfully");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Mark all notifications as read for specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized token, User not found")
    })
    @PatchMapping("/mark-all")
    public ResponseEntity<HashMap<String, Object>> markAllAsRead(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();

        Long userId = jwtService.extractUserIdAsLong(header.substring(7));

        notificationService.markAllAsRead(userId.intValue());
        responseMap.put("Message", "marked as read successfully");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
