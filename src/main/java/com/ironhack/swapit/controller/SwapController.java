package com.ironhack.swapit.controller;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/swap")
@RequiredArgsConstructor
public class SwapController {

    // Service Instantiations
    private final ItemService itemService;
    private final UserService userService;


    // GET Methods
    @GetMapping("/{username}")
    public Item getRandom(@PathVariable("username") String username) {
        return itemService.findRandomItemToSwap(username);
    }
}
