package org.bringme.repository;

import org.bringme.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getAll();
    Optional<Item> getById(Long id);
    Long saveItem(Item item);
    List<Item> filterByCountries(int origin, int destination);


}
