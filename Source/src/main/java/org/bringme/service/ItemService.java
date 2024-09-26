package org.bringme.service;

import org.bringme.dto.ItemDTO;
import org.bringme.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAll();
    Item getItemById(Long id);
    Long saveItem(ItemDTO itemDTO);
}
