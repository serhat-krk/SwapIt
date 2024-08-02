package com.ironhack.swapit.controller;

import com.ironhack.swapit.dto.request.ItemRequest;
import com.ironhack.swapit.model.Book;
import com.ironhack.swapit.model.Clothing;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.UserService;
import com.ironhack.swapit.service.impl.ItemServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @PreAuthorize("#username == authentication.name or hasRole('ROLE_ADMIN')")
    public List<Item> getByUser(@PathVariable("username") String username) {
        return itemService.findUserItems(username);
    }


// POST Mapping

    /** LIST
     * Create a new item (book or clothing) for the logged-in user
     * @param itemListRequest dto for item details
     * @return saved item
     * @secured logged-in user
     */
    @PostMapping("/list")
    @ResponseStatus(HttpStatus.CREATED)
    public Item list(@RequestBody @Valid ItemRequest itemListRequest) {

        // Find username of logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Initiate item variable with switch condition
        Item itemToList = switch (itemListRequest.getItemClass()) {

            case BOOK -> new Book(
                    itemListRequest.getTitle(),
                    itemListRequest.getDescription(),
                    userService.findByUsername(username),
                    BOOK,
                    itemListRequest.getAuthor(),
                    itemListRequest.getGenre());

            case CLOTHING -> new Clothing(
                    itemListRequest.getTitle(),
                    itemListRequest.getDescription(),
                    userService.findByUsername(username),
                    CLOTHING,
                    itemListRequest.getCategory(),
                    itemListRequest.getType(),
                    itemListRequest.getSize());

            default -> null;

        };

        // Save item to database
        return itemService.save(itemToList);

    }


// PUT Mapping

    /** UPDATE
     * Change item details (book or clothing) of the logged-in user
     * @param itemId of item to update
     * @param itemUpdateRequest body for updates
     * @return updated item
     * @secured item owner (logged-in user) or admin
     */
    @PutMapping("/id/{id}/update")
    @PreAuthorize("@itemServiceImpl.isOwner(#itemId) or hasRole('ROLE_ADMIN')")
    public Item update(@PathVariable("id") int itemId, @RequestBody @Valid ItemRequest itemUpdateRequest) {

        // Find username of logged-in user and item to update
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Item itemToUpdate = itemService.findById(itemId).orElseThrow();

        // Use separate update methods based on item class
        switch (itemToUpdate.getItemClass()) {
            case BOOK -> itemService.updateBook(itemId, itemUpdateRequest);
            case CLOTHING -> itemService.updateClothing(itemId, itemUpdateRequest);
        }

        // Return updated item
        return itemService.findById(itemId).orElseThrow();
    }


// DELETE Mapping

    // Delete an item, for owner (logged-in user) or admins
    @DeleteMapping("id/{id}/delete")
    @PreAuthorize("@itemServiceImpl.isOwner(#itemId) or hasRole('ROLE_ADMIN')")
    public void deleteById(@PathVariable("id") int itemId) {
        itemService.deleteById(itemId);
    }

}