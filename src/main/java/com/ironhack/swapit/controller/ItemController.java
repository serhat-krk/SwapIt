package com.ironhack.swapit.controller;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    // Service Instantiation
    private final ItemService itemService;


// GET Mappings

    // Return list of all items, for admins
    @GetMapping
    @Secured("ROLE_ADMIN")
    public List<Item> getAll() {
        return itemService.findAll();
    }

    // Return an item by id, for the owner or admins
    @GetMapping("/id/{id}")
    @PreAuthorize("@itemServiceImpl.isOwner(#itemId) or hasRole('ROLE_ADMIN')")
    public Optional<Item> getById(@PathVariable("id") int itemId) {
        return itemService.findById(itemId);
    }

    // Return all items of a user by username, for the owner or admins
    @GetMapping("/user/{username}")
    @PreAuthorize("#username == authentication.principal or hasRole('ROLE_ADMIN')")
    public List<Item> getByUser(@PathVariable("username") String username) {
        return itemService.findUserItems(username);
    }


// POST Mappings

    @PostMapping("/list")
    @ResponseStatus(HttpStatus.CREATED)
    public Item post(@RequestBody @Valid Item item) {
        return itemService.save(item);
    }


// PUT Mappings


// PATCH Mappings


// DELETE Mappings

}