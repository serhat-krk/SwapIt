package com.ironhack.swapit.controller;

import com.ironhack.swapit.dto.LikeRequest;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.MatchService;
import com.ironhack.swapit.service.SwapService;
import com.ironhack.swapit.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

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
        logger.debug("Logged-in user: {}", username);

        // Find itemId from like request
        int itemId = likeRequest.getItemId();
        logger.debug("Item ID from request: {}", itemId);

        // Throw unauthorized error if user likes their own item
        Item likedItem = itemService.findById(itemId).orElseThrow();
        if (likedItem.getOwner().getUsername().equals(username))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Users cannot like their own items");

        logger.debug("{} likes item: {}", username, likedItem.getItemId());

        // Add user to likedBy set of item
        swapService.like(username, itemId);
        logger.debug("Item {} successfully liked by {}", likedItem.getItemId(), username);

        // Check if there are matching items
        User likerUser = userService.findByUsername(username);
        logger.debug("User1 (likerUser): {}", likerUser.getUsername());

        User likedUser = itemService.findById(itemId).orElseThrow().getOwner();
        logger.debug("User2 (likedItem owner): {}", likedUser.getUsername());

        // Re-fetch users to ensure we have the latest state
        likerUser = userService.findByUsername(username);
        likedUser = userService.findByUsername(likedUser.getUsername());

        matchService.createMatchIfMutualLike(likerUser, likedUser);
        logger.debug("Checked for mutual matches between users: {} and {}", likerUser.getUsername(), likedUser.getUsername());

    }

}
