package com.ironhack.swapit.controller;

import com.ironhack.swapit.dto.LikeRequest;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.MatchService;
import com.ironhack.swapit.service.SwapService;
import com.ironhack.swapit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

// GET Mappings

    /**
     * Show a random item from other users, initial pathway to Like operation
     * @return a random item from other users
     * @secured logged-in user
     * TODO: add filters
     */
    @GetMapping
    public Item getRandom() {

        // Find username of logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Return random item that does not belong to user
        return swapService.findRandomItem(username);
    }


// POST Mappings

    // TODO: Find out a way to do this without LikeRequest DTO
    @PostMapping("/like")
    public void like(@RequestBody LikeRequest likeRequest) {

        // Find username of logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find itemId from like request
        int itemId = likeRequest.getItemId();

        // Throw unauthorized error if user likes their own item
        Item item = itemService.findById(itemId).orElseThrow();
        if (item.getOwner().getUsername().equals(username))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Users cannot like their own items");

        // Add user to likedBy set of item
        swapService.like(username, itemId);
    }

}
