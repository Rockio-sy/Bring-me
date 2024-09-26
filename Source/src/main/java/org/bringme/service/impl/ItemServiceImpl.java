package org.bringme.service.impl;

import org.bringme.dto.ItemDTO;
import org.bringme.model.Item;
import org.bringme.repository.ItemRepository;
import org.bringme.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.getAll();
    }

    @Override
    public Item getItemById(Long id) {
        Optional<Item> newItem = itemRepository.getById(id);
        return newItem.orElse(null);
    }

    @Override
    public Long saveItem(ItemDTO itemDTO) {
        System.out.println("DEBUG:\n In Service:\n\t function : saveItem.");
        Item newItem = new Item(
                null,
                itemDTO.getName(),
                itemDTO.getOrigin(),
                itemDTO.getDestination(),
                itemDTO.getWeight(),
                itemDTO.getHeight(),
                itemDTO.getLength(),
                itemDTO.getComments(),
                itemDTO.getDetailedOriginAddress(),
                itemDTO.getPhoto(),
                itemDTO.getUser_id()
        );
        return itemRepository.saveItem(newItem);
    }
}
