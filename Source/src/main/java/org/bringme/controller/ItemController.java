package org.bringme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.bringme.dto.ItemDTO;
import org.bringme.service.ItemService;
import org.bringme.service.impl.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/bring-me/items")
public class ItemController {
    private final ItemService itemService;
    private final JwtService jwtService;

    public ItemController(ItemService itemService, JwtService jwtService) {
        this.itemService = itemService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Create new item", description = "Create item after uploading valid image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad request, image not found, invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "415", description = "Unsupported media file")
    })
    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewItem(@RequestHeader(value = "Authorization") String header,
                                                                 @Valid @RequestBody ItemDTO requestedItem)
    {
        // multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Get the user id and set it in the request body
        Long userId = jwtService.extractUserIdAsLong(header.substring(7));
        requestedItem.setUser_id(userId);

        // Saving item and return id
        ItemDTO responseDTO = itemService.saveItem(requestedItem);

        responseMap.put("Item", responseDTO);
        responseMap.put("Message", "Item created successfully.");
        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }


    @Operation(summary = "Upload image", description = "Upload temp image to create new item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Uploaded", content = @Content(mediaType = "Multipart form data value")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad request, file is empty, invalid metadata"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, token errors"),
            @ApiResponse(responseCode = "415", description = "Unsupported media type"),
            @ApiResponse(responseCode = "413", description = "Payload too large")
    })
    @PostMapping(value = "/new/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HashMap<String, Object>> uploadPhoto(@Valid @RequestParam("image") MultipartFile image) {
        HashMap<String, Object> responseMap = new HashMap<>();
        String tempFileName = itemService.checkMediaFile(image);
        responseMap.put("Message", "Temp photo uploaded successfully");
        responseMap.put("File-name", tempFileName);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Get all items as list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "204", description = "no content")
    })
    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAll() {
        HashMap<String, Object> responseMap = new HashMap<>();
        List<ItemDTO> responseList = itemService.getAll();
        responseMap.put("Message", "List found successfully.");
        responseMap.put("Items", responseList);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Show an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "203", description = "Item not found")
    })
    @GetMapping("/show")
    public ResponseEntity<HashMap<String, Object>> getItemById(@RequestParam @Positive Long id) {
        HashMap<String, Object> responseMap = new HashMap<>();

        ItemDTO responseItem = itemService.getItemById(id);
        responseMap.put("Message", "Item returned successfully.");
        responseMap.put("Item", responseItem);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Operation(summary = "Search filter by countries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "203", description = "Not found, empty list"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input")
    })
    @GetMapping("/filter/by/countries")
    public ResponseEntity<HashMap<String, Object>> getByCountry(@RequestParam(name = "from") @Positive int origin,
                                                                @RequestParam(name = "to") @Positive int destination)
    {
        HashMap<String, Object> responseMap = new HashMap<>();

        List<ItemDTO> response = itemService.filterByCountries(origin, destination);
        responseMap.put("Message", "Data returned successfully");
        responseMap.put("Items", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
