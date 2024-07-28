package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.ItemRepository;
import com.ironhack.swapit.repository.MatchRepository;
import com.ironhack.swapit.repository.UserRepository;
import com.ironhack.swapit.service.SwapService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SwapServiceImpl implements SwapService {

    // Repository Instantiations
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final MatchRepository matchRepository;

    private final EntityManager entityManager;


// Methods


    // Find a random item that does not belong to user
    @Override
    public Item findRandomItem(String username) {
        return itemRepository.findRandomItem(username);
    }


    /** LIKE
     * Add item to likedItems set variable of user
     * @param username
     * @param itemId
     */
    @Override
    @Transactional
    public void like(String username, int itemId) {

        // Retrieve the user and item objects from the repository
        User user = userRepository.findByUsername(username);
        Item item = itemRepository.findById(itemId).orElseThrow();

        // Add the item to the user's likedItems set
        user.getLikedItems().add(item);

        // Save the item to persist the changes
        userRepository.save(user);

        // Ensure that changes are flushed to the database
        entityManager.flush();

        // Clear the persistence context to avoid stale data
        entityManager.clear();

    }



}
