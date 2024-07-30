package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.Role;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.ItemRepository;
import com.ironhack.swapit.repository.UserRepository;
import com.ironhack.swapit.service.ItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    // Repository Instantiation
    private final ItemRepository itemRepository;


// GET methods

    public List<Item> findAll() {
        log.info("Fetching all items");
        return itemRepository.findAll();
    }

    public Optional<Item> findById(int id) {
        log.info("Fetching item by ID: {}", id);
        return itemRepository.findById(id);
    }

    public List<Item> findUserItems(String username) {
        log.info("Fetching all items of user: {}", username);
        return itemRepository.findItemByOwner_Username(username);
    }


// POST Methods

    @Transactional
    public Item save(Item item) {
        log.info("Saving new item with ID {} to the database", item.getItemId());
        return itemRepository.save(item);
    }


// PUT Methods

//    @Transactional
//    public Item update(int itemId, Item item) {
//
//    }

// Other Methods

    /**
     * Security check if logged-in user owns the item
     * @return True: when user is owner
     *         False: when user is not owner
     */
    public boolean isOwner(int itemId) {

        // Find username of logged-in user
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find item by itemId
        Item item = itemRepository.findById(itemId).orElseThrow();

        // Return true if item owner username equals logged-in user username, return false if not
        return item.getOwner().getUsername().equals(currentUsername);
    }

}