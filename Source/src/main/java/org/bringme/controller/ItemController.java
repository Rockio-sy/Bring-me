package org.bringme.controller;

import org.bringme.dto.ItemDTO;
import org.bringme.model.Item;
import org.bringme.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("bring-me/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/new")
    public ResponseEntity<HashMap<String, Object>> createNewItem(@RequestBody ItemDTO itemDTO) {
        // multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Data checking
        if (itemDTO.getLength() <= 0 || itemDTO.getWeight() <= 0 || itemDTO.getHeight() <= 0
                || itemDTO.getLength() <= 2 || itemDTO.getWeight() <= 2 || itemDTO.getHeight() >= 2
                || itemDTO.getOrigin() == itemDTO.getDestination()) {
            responseMap.put("Status", "422");
            responseMap.put("Error message", "Invalid data");
            return new ResponseEntity<>(responseMap, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Getting item id
        Long generatedId = itemService.saveItem(itemDTO);
        if (generatedId != null) {
            responseMap.put("Status", "201");
            responseMap.put("Item", itemDTO);
            responseMap.put("Message", "Item created successfully.");
            responseMap.put("id", generatedId);

            return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAll() {
        List<Item> items = itemService.getAll();
        if (items.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(items, HttpStatus.OK);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item = itemService.getItemById(id);
        if (item != null) {
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
