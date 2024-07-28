package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.Role;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.ItemRepository;
import com.ironhack.swapit.repository.UserRepository;
import com.ironhack.swapit.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    // Repository Instantiation
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


// GET methods

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> findById(int id) {
        return itemRepository.findById(id);
    }

    // To get all items of a user
    @Override
    public List<Item> findUserItems(String username) {
        return itemRepository.findItemByOwner_Username(username);
    }

    // To find a random item that does not belong to user
    @Override
    public Item findRandomItem(String username) {
        return itemRepository.findRandomItem(username);
    }


// POST Methods

    // To save new item
    // TODO: Owner must be the logged-in user only
    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

//    /**
//     * add user to likedBy set variable of item
//     * @param username
//     * @param itemId
//     */
//    // TODO: IT DOESN'T WORK, FIX IT
//    @Override
//    public void like(String username, int itemId) {
//
//        // Retrieve the user and item objects from the repository
//        User user = userRepository.findByUsername(username);
//        Item item = itemRepository.findById(itemId).orElseThrow();
//
//        // Throw unauthorized error if user likes their own item
//        if (item.getOwner().getUsername().equals(username))
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Users cannot like their own items");
//
//        // Add the user to the item's like set
//        item.getLikedBy().add(user);
//
//        // Save the item to persist the changes
//        itemRepository.save(item);
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
