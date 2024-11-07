package org.bringme.controller;

import com.sun.source.tree.NewArrayTree;
import jakarta.validation.Valid;
import org.bringme.dto.NotificationDTO;
import org.bringme.service.NotificationService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/bring-me/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final JwtService jwtService;

    public NotificationController(NotificationService notificationService, JwtService jwtService) {
        this.notificationService = notificationService;
        this.jwtService = jwtService;
    }

    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAllNotifications(@Valid @RequestHeader(value = "Authorization") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();

        // Validate
        if (header == null || !header.startsWith("Bearer")) {
            responseMap.put("Message", "Invalid token.");
            return new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
        }
        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);

        List<NotificationDTO> all = notificationService.getAll(userId);

        if (all.isEmpty()) {
            responseMap.put("Message", "No content");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }
        responseMap.put("Notifications", all);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/not-marked")
    public ResponseEntity<HashMap<String, Object>> getNotMarkedNotifications(@Valid @RequestHeader(value = "Authorizations") String header) {
        HashMap<String, Object> responseMap = new HashMap<>();

        // Validate
        if (header == null || !header.startsWith("Bearer")) {
            responseMap.put("Message", "Invalid token.");
            return new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
        }
        String token = header.substring(7);
        Long userId = jwtService.extractUserIdAsLong(token);

        List<NotificationDTO> notMarked = notificationService.getNotMarked(userId);

        if (notMarked.isEmpty()) {
            responseMap.put("Message", "No content");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        }

        responseMap.put("Not-marked", notMarked);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }


}
