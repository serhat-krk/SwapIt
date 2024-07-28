package com.ironhack.swapit.controller;

import com.ironhack.swapit.dto.LikeRequest;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.MatchService;
import com.ironhack.swapit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/swap")
@RequiredArgsConstructor
public class SwapController {

    // Service Instantiations
    private final ItemService itemService;
    private final UserService userService;
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
        return itemService.findRandomItem(username);
    }


// PATCH Mappings

    @PatchMapping("/like")
    public void like(@RequestBody LikeRequest likeRequest) {
        Item likedItem = itemService.findById(likeRequest.getItemId()).orElseThrow();
        User likerUser = userService.findByUsername(likeRequest.getUsername());
        itemService.like(likerUser, likedItem);
    }

}
