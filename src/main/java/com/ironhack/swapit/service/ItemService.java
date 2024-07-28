package com.ironhack.swapit.service;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemService {

    // GET methods
    List<Item> findAll();
    Optional<Item> findById(int id);
    List<Item> findUserItems(String username);
    Item findRandomItem(String username);

    // POST methods
    Item save(Item item);

    // PUT methods

    // PATCH methods

    // DELETE methods

}
