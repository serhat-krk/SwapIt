package com.ironhack.swapit.controller;

import com.ironhack.swapit.dto.LikeRequest;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.MatchService;
import com.ironhack.swapit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/swap")
@RequiredArgsConstructor
public class SwapController {

    // Service Instantiations
    private final ItemService itemService;
    private final UserService userService;
    private final MatchService matchService;


    /**
     * show a random item from other users
     * @param username the operator
     * @return a random item from other users
     */
    @GetMapping
    public Item getRandom(@RequestBody String username) {
        return itemService.findRandomItem(username);
    }


    // PATCH Methods
    @PatchMapping("/like")
    public void like(@RequestBody LikeRequest likeRequest) {
        Item likedItem = itemService.findById(likeRequest.getItemId()).orElseThrow();
        User likerUser = userService.findByUsername(likeRequest.getUsername());
        itemService.like(likerUser, likedItem);
    }

}
