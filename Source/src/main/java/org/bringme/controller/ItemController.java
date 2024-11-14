package org.bringme.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
// TODO: Try all endpoints after changing clean the code
    private final ItemService itemService;
    private final JwtService jwtService;

    public ItemController(ItemService itemService, JwtService jwtService) {
        this.itemService = itemService;
        this.jwtService = jwtService;
    }

    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewItem(@RequestHeader(value = "Authorization") String header, @Valid @RequestBody ItemDTO requestedItem) {
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

    @PostMapping(value = "/new/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HashMap<String, Object>> uploadPhoto(@Valid @RequestParam("image") MultipartFile image) {
        HashMap<String, Object> responseMap = new HashMap<>();
        String tempFileName = itemService.checkMediaFile(image);
        responseMap.put("Message", "Temp photo uploaded successfully");
        responseMap.put("File-name", tempFileName);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAll() {
        HashMap<String, Object> responseMap = new HashMap<>();
        List<ItemDTO> responseList = itemService.getAll();
        responseMap.put("Message", "List found successfully.");
        responseMap.put("Items", responseList);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<HashMap<String, Object>> getItemById(@PathVariable @Min(1) Long id) {
        // Response map
        HashMap<String, Object> responseMap = new HashMap<>();
        // Creating response
        ItemDTO responseItem = itemService.getItemById(id);
        responseMap.put("Status", "200");
        responseMap.put("Message", "Item returned successfully.");
        responseMap.put("Item", responseItem);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/filter/{from}/{to}")
    public ResponseEntity<HashMap<String, Object>> getByCountry(@PathVariable(name = "from") @Min(1) int origin, @PathVariable(name = "to") @Min(1)int destination) {
        // Multi-value map
        HashMap<String, Object> responseMap = new HashMap<>();
        // getting list of items
        List<ItemDTO> response = itemService.filterByCountries(origin, destination);
        responseMap.put("Message", "Data returned successfully");
        responseMap.put("Items", response);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
