package com.ironhack.swapit.service;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    // Repository Instantiation
    private final ItemRepository itemRepository;


    // GET methods
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(UUID id) {
        return itemRepository.findById(id);
    }


    // POST methods
    public Item save(Item item) {
        return itemRepository.save(item);
    }


    // PUT methods


    // PATCH methods


    // DELETE methods

}
