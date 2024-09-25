package org.bringme.controller;

import org.bringme.model.Item;
import org.bringme.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bring-me/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAll(){
        List<Item> items = itemService.getAll();
        if(items.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(items, HttpStatus.OK);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Item> createNewItem(@RequestBody Item item){
        Item newItem = itemService.saveItem(item);
        if(newItem != null){
            return new ResponseEntity<>(newItem, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id){
        Item item = itemService.getItemById(id);
        if(item != null){
            return new ResponseEntity<>(item, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
