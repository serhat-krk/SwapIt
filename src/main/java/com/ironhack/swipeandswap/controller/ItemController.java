package com.ironhack.swipeandswap.controller;

import com.ironhack.swipeandswap.model.Item;
import com.ironhack.swipeandswap.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("items")
@RequiredArgsConstructor
public class ItemController {

    // Service Instantiation
    private final ItemService itemService;


    // GET Mappings
    @GetMapping
    public List<Item> getAll() {
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Item> getById(@PathVariable("id") UUID id) {
        return itemService.findById(id);
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
