package org.bringme.service;

import org.bringme.dto.ItemDTO;
import org.bringme.model.Item;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    List<Item> getAll();
    Item getItemById(Long id);
    ItemDTO saveItem(ItemDTO itemDTO);
    String saveTempFile(MultipartFile image);
}
