package org.bringme.service;

import org.bringme.dto.ItemDTO;
import org.bringme.model.Item;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    List<ItemDTO> getAll();
    ItemDTO getItemById(Long id);
    ItemDTO saveItem(ItemDTO itemDTO);
    String saveTempFile(MultipartFile image);

    List<ItemDTO> filterByCountries(int origin, int destination);

    void checkInput(ItemDTO requestItem);

    String checkMediaFile(MultipartFile image);
}
