package com.ironhack.swapit.service;

import com.ironhack.swapit.dto.display.ItemDisplay;
import com.ironhack.swapit.dto.request.ItemRequest;
import com.ironhack.swapit.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    // GET methods
    List<Item> findAll();
    Optional<Item> findById(int itemId);
    List<Item> findUserItems(String username);

    // POST method
    Item save(Item item);

    // PUT methods
    void updateBook(int itemId, ItemRequest updatedBook);
    void updateClothing(int itemId, ItemRequest updatedClothing);

    // DELETE method
    void deleteById(int itemId);

    // Other methods
    boolean isOwner(int itemId);
    ItemDisplay createDisplayItem(Item item);

}