package org.bringme.controller;

import jakarta.validation.Valid;
import org.bringme.dto.ItemDTO;
import org.bringme.model.Item;
import org.bringme.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    public ResponseEntity<HashMap<String, Object>> createNewItem(@Valid @RequestBody ItemDTO requestItem) {
        // multi value map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Data checking
        if (requestItem.getLength() <= 0 || requestItem.getWeight() <= 0 || requestItem.getHeight() <= 0
                || requestItem.getLength() >= 2 || requestItem.getWeight() >= 5 || requestItem.getHeight() >= 2
                || requestItem.getOrigin() == requestItem.getDestination() || requestItem.getUser_id() == 0
                || requestItem.getOrigin() == 0 || requestItem.getDestination() == 0) {
            responseMap.put("Status", "422");
            responseMap.put("Error message", "Invalid data");
            return new ResponseEntity<>(responseMap, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // Saving item and return id
        ItemDTO responseDTO = itemService.saveItem(requestItem);
        if (responseDTO.getId() == null) {
            responseMap.put("Status", "500");
            responseMap.put("Message", "Unknown error");
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        responseMap.put("Item", responseDTO);
        responseMap.put("Status", "201");
        responseMap.put("Message", "Item created successfully.");

        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);

    }


    @PostMapping(value = "/new/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HashMap<String, Object>> uploadPhoto(@Valid @RequestParam("image") MultipartFile image) {
        HashMap<String, Object> responseMap = new HashMap<>();
        try {
            // Check file status
            if (!(image.isEmpty() || image.getOriginalFilename() == null || image.getContentType() == null)) {

                // Check file size
                if (!(image.getSize() > (2 * 1024 * 1024))) {

                    // check file type
                    String contentType = image.getContentType();
                    if (contentType.startsWith("image/png") || contentType.startsWith("image/jpeg")) {
                        String tempFileName = itemService.saveTempFile(image);
                        if (tempFileName != null) {
                            responseMap.put("Status", "200 OK");
                            responseMap.put("Message", "Temp photo uploaded successfully");
                            responseMap.put("File-name", tempFileName);
                            return new ResponseEntity<>(responseMap, HttpStatus.OK);
                        } else {
                            responseMap.put("Status", "500 INTERNAL SERVER ERROR");
                            responseMap.put("Message", "Error uploading file");
                            responseMap.put("File-name", null);
                            System.out.println("HERE IS THE ERROR");
                            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        responseMap.put("Status", "415 UNSUPPORTED MEDIA TYPE");
                        responseMap.put("Message", "PNG or JPEG only!");
                        responseMap.put("File-name", null);

                        return new ResponseEntity<>(responseMap, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
                    }

                } else {
                    responseMap.put("Status", "413 PAYLOAD TOO LARGE");
                    responseMap.put("Message", "File size should be 2MB or less");
                    responseMap.put("File-name", null);
                    return new ResponseEntity<>(responseMap, HttpStatus.PAYLOAD_TOO_LARGE);
                }

            } else {
                responseMap.put("Status", "400 BAD REQUEST");
                responseMap.put("Message", "File is empty.");
                responseMap.put("File-name", null);
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            responseMap.put("Status", "500 INTERNAL SERVER ERROR");
            responseMap.put("Message", "Error uploading file");
            responseMap.put("File-name", null);
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAll() {
//        System.out.println("In getAll controller");
        HashMap<String, Object> responseMap = new HashMap<>();
        List<ItemDTO> responseList = itemService.getAll();

        if (responseList.isEmpty()) {
            responseMap.put("Status", "404");
            responseMap.put("Message", "No content");
            return new ResponseEntity<>(responseMap, HttpStatus.NO_CONTENT);
        } else {
            responseMap.put("Status", "200");
            responseMap.put("Message", "List of items returned successfully.");
            responseMap.put("Count of items", responseList.size());
            responseMap.put("Items", responseList);
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<HashMap<String, Object>> getItemById(@PathVariable Long id) {
        // Response map
        HashMap<String, Object> responseMap = new HashMap<>();

        // Creating response
        ItemDTO responseItem = itemService.getItemById(id);
        if (responseItem != null) {
            responseMap.put("Status", "200");
            responseMap.put("Message", "Item returned successfully.");
            responseMap.put("Item", responseItem);
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
