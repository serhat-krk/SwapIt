package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.dto.display.ItemDisplay;
import com.ironhack.swapit.dto.display.UserDisplay;
import com.ironhack.swapit.dto.request.ItemRequest;
import com.ironhack.swapit.model.*;
import com.ironhack.swapit.repository.ItemRepository;
import com.ironhack.swapit.service.ItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    // Repository Instantiation
    private final ItemRepository itemRepository;


// GET methods

    public List<Item> findAll() {
        log.info("Fetching all items");
        return itemRepository.findAll();
    }

    public Optional<Item> findById(int id) {
        log.info("Fetching item by ID: {}", id);
        return itemRepository.findById(id);
    }

    public List<Item> findUserItems(String username) {
        log.info("Fetching all items of user: {}", username);
        return itemRepository.findByOwner_Username(username);
    }


// POST Method

    @Transactional
    public Item save(Item item) {
        log.info("Saving new item with ID {} to the database", item.getItemId());
        return itemRepository.save(item);
    }


// PUT Methods

    @Transactional
    public void updateBook(int itemId, ItemRequest updatedBook) {
        log.info("Updating book item {} details in the database", itemId);

        // Find book in database
        Book bookToUpdate = (Book) itemRepository.findById(itemId).orElseThrow();

        // Update non-null book details
        if (updatedBook.getTitle() != null)
            bookToUpdate.setTitle(updatedBook.getTitle());
        if (updatedBook.getDescription() != null)
            bookToUpdate.setDescription(updatedBook.getDescription());
        if (updatedBook.getAuthor() != null)
            bookToUpdate.setAuthor(updatedBook.getAuthor());
        if (updatedBook.getGenre() != null)
            bookToUpdate.setGenre(updatedBook.getGenre());

        // Save updated book to database
        itemRepository.save(bookToUpdate);
    }

    @Transactional
    public void updateClothing(int itemId, ItemRequest updatedClothing) {
        log.info("Updating clothing item {} details in the database", itemId);

        // Find clothing in database
        Clothing clothingToUpdate = (Clothing) itemRepository.findById(itemId).orElseThrow();

        // Update non-null clothing details
        if (updatedClothing.getTitle() != null)
          clothingToUpdate.setTitle(updatedClothing.getTitle());
        if (updatedClothing.getDescription() != null)
            clothingToUpdate.setDescription(updatedClothing.getDescription());
        if (updatedClothing.getCategory() != null)
            clothingToUpdate.setCategory(updatedClothing.getCategory());
        if (updatedClothing.getType() != null)
            clothingToUpdate.setType(updatedClothing.getType());
        if (updatedClothing.getSize() != null)
            clothingToUpdate.setSize(updatedClothing.getSize());

        // Save updated clothing to database
        itemRepository.save(clothingToUpdate);
    }


// DELETE Method

    @Transactional
    public void deleteById(int itemId) {

        // Find item to delete by ID
        Item itemToDelete = itemRepository.findById(itemId).orElseThrow();

        // Disassociate item from owner
        itemToDelete.setOwner(null);
        itemRepository.save(itemToDelete);

        // Delete item
        itemRepository.deleteById(itemId);
    }


// Other Methods

    /**
     * Security check if logged-in user owns the item
     * @return True: when user is owner
     *         False: when user is not owner
     */
    public boolean isOwner(int itemId) {

        // Find username of logged-in user
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find item by itemId
        Item item = itemRepository.findById(itemId).orElseThrow();

        // Return true if item owner username equals logged-in user username, return false if not
        return item.getOwner().getUsername().equals(currentUsername);
    }

    // Create display item
    public ItemDisplay createDisplayItem(Item item) {

        // Create display item with shared book and clothing properties
        ItemDisplay itemDisplay = new ItemDisplay(
                item.getItemId(),
                item.getTitle(),
                item.getDescription(),

                // Create display user
                new UserDisplay(
                        item.getOwner().getName(),
                        item.getOwner().getCity(),
                        item.getOwner().getEmail()
                ),

                item.getItemClass()
        );

        // Update display item with book OR clothing properties
        switch (item.getItemClass()) {

            case BOOK -> {
                Book bookFound = (Book) item;
                itemDisplay.setGenre(bookFound.getGenre());
                itemDisplay.setAuthor(bookFound.getAuthor());
            }

            case CLOTHING -> {
                Clothing clothingFound = (Clothing) item;
                itemDisplay.setCategory(clothingFound.getCategory());
                itemDisplay.setType(clothingFound.getType());
                itemDisplay.setSize(clothingFound.getSize());
            }

        }

        // Return updated display item
        return itemDisplay;

    }

}