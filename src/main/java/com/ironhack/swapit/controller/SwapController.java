package com.ironhack.swapit.controller;

import com.ironhack.swapit.dto.display.ItemDisplay;
import com.ironhack.swapit.dto.request.LikeRequest;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.MatchService;
import com.ironhack.swapit.service.SwapService;
import com.ironhack.swapit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/swap")
@RequiredArgsConstructor
public class SwapController {

    // Service Instantiations
    private final ItemService itemService;
    private final UserService userService;
    private final SwapService swapService;
    private final MatchService matchService;

    private static final Logger logger = LoggerFactory.getLogger(SwapController.class);

// GET Mappings

    /** FIND RANDOM
     * Show a random item from other users, initial pathway to Like operation
     * @return a random item from other users
     * @secured logged-in user
     * TODO: add filters
     */
    @GetMapping
    public ItemDisplay getRandom() {

        // Find username of logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Return random item that does not belong to user
        return swapService.findRandomItem(username);
    }


// POST Mappings

    // TODO: Find out a way to do this without LikeRequest DTO
    /** LIKE
     * Add item to liked items collection of logged-in user using item ID
     * @param likeRequest dto for item ID
     * @secured logged-in user
     */
    @PostMapping("/like")
    public void like(@RequestBody LikeRequest likeRequest) {

        // Find username of logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find liked item
        Item likedItem = itemService.findById(likeRequest.getItemId()).orElseThrow();

        // Throw unauthorized error if user likes their own item
        if (likedItem.getOwner().getUsername().equals(username))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Users cannot like their own items");

        // Save like
        swapService.like(username, likedItem.getItemId());

        // Check matches after like is saved
        matchService.createMatchIfMutualLike(userService.findByUsername(username), likedItem.getOwner());

    }

}