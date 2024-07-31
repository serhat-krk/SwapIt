package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.dto.display.ItemDisplay;
import com.ironhack.swapit.dto.display.UserDisplay;
import com.ironhack.swapit.enums.ItemClass;
import com.ironhack.swapit.model.Book;
import com.ironhack.swapit.model.Clothing;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.ItemRepository;
import com.ironhack.swapit.repository.MatchRepository;
import com.ironhack.swapit.repository.UserRepository;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.SwapService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.ironhack.swapit.enums.ItemClass.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SwapServiceImpl implements SwapService {

    // Repository Instantiations
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final MatchRepository matchRepository;
    private final ItemService itemService;


// Methods

    // Find a random item that does not belong to user
    @Override
    public ItemDisplay findRandomItem(String username) {

        // Fetch random item
        Item itemFound = itemRepository.findRandomItem(username);

        // Return item in display format
        return itemService.createDisplayItem(itemFound);

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

        // Add the item to the user's likedItems
        user.getLikedItems().add(item);

        // Update the user in database
        userRepository.save(user);

    }

}