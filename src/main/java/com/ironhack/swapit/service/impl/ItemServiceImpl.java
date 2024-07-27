package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.repository.ItemRepository;
import com.ironhack.swapit.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    // Repository Instantiation
    private final ItemRepository itemRepository;


    // GET methods
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(int id) {
        return itemRepository.findById(id);
    }

    // To get all items of a user
    public List<Item> findUserItems(String username) {
        return itemRepository.findUserItems(username);
    }

    // To find a random item that does not belong to user
    public Item findRandomItemToSwap(String username) {
        return itemRepository.findRandomItemToSwap(username);
    }


    // POST methods
    public Item save(Item item) {
        return itemRepository.save(item);
    }
}
