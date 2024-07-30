package com.ironhack.swapit.controller;

import com.ironhack.swapit.dto.ItemListRequest;
import com.ironhack.swapit.enums.ItemClass;
import com.ironhack.swapit.model.Book;
import com.ironhack.swapit.model.Clothing;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ironhack.swapit.enums.ItemClass.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    // Service Instantiation
    private final ItemService itemService;
    private final UserService userService;


// GET Mappings

    // Return list of all items, for admins
    @GetMapping("/all")
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

    /** LIST
     * Create a new item (book or clothing) for the logged-in user
     * @param itemListRequest dto for item details
     * @return listed item from database
     * @secured logged-in user
     */
    @PostMapping("/list")
    @ResponseStatus(HttpStatus.CREATED)
    public Item list(@RequestBody @Valid ItemListRequest itemListRequest) {

        // Find username of logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Initiate null item variable
        Item itemToList = null;

        // Update item variable as Book
        if (itemListRequest.getItemClass().equals(BOOK)) {
            itemToList = new Book(
                    itemListRequest.getTitle(),
                    itemListRequest.getDescription(),
                    userService.findByUsername(username),
                    itemListRequest.getAuthor(),
                    itemListRequest.getGenre());
        }

        // Update item variable as Clothing
        if (itemListRequest.getItemClass().equals(CLOTHING)) {
            itemToList = new Clothing(
                    itemListRequest.getTitle(),
                    itemListRequest.getDescription(),
                    userService.findByUsername(username),
                    itemListRequest.getCategory(),
                    itemListRequest.getType(),
                    itemListRequest.getSize());
        }

        // Save item
        return itemService.save(itemToList);

    }


// PUT Mappings

//    @PutMapping("/id/{id}")
//    public Item update(@PathVariable("id") int itemId, @RequestBody @Valid Item updatedItem) {
//
//    }


// PATCH Mappings


// DELETE Mappings

}