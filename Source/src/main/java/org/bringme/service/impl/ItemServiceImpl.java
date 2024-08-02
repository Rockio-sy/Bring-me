package org.bringme.service.impl;

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
    public Item saveItem(Item item) {
        int rowAffected = itemRepository.saveItem(item);
        return (rowAffected > 0) ? item : null;
    }
}
