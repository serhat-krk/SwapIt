package com.ironhack.swapit.controller;

import com.ironhack.swapit.model.User;
import com.ironhack.swapit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    // Service Instantiation
    private final UserService userService;


// GET Mappings

    // Return list of all users, for admins
    @GetMapping("/users/all")
    @Secured("ROLE_ADMIN")
    public List<User> getAll() {
        return userService.findAll();
    }

    // Return list of all users from a city, for admins
    @GetMapping("/users/by-city")
    @Secured("ROLE_ADMIN")
    public List<User> getByCity(@RequestParam("city") String city) {
        return userService.findByCity(city);
    }

    // Return a user by username, for logged-in user or admins
    @GetMapping("/users/{username}")
    @PreAuthorize("#username == authentication.name or hasRole('ROLE_ADMIN')")
    public User getByUsername(@PathVariable("username") String username) {
        return userService.findByUsername(username);
    }


// POST Mappings

    // Register a new user, public endpoint
    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public User save(@RequestBody @Valid User user) {
        return userService.save(user);
    }


// PUT Mappings

    // Update all user information, for logged-in user
    @PutMapping("/users/{username}/update")
    @PreAuthorize("#username == authentication.name")
    public User update(@PathVariable("username") String username, @RequestBody @Valid User updatedUser) {
        return userService.update(username, updatedUser);
    }


// PATCH Mappings

    // Update user city, for logged-in user
    @PatchMapping("/users/{username}/update-city")
    @PreAuthorize("#username == authentication.name")
    public User updateCity(@PathVariable("username") String username, @RequestBody @Valid String newCity) {
        return userService.updateCity(username, newCity);
    }


// DELETE Mappings

    // Delete user from database, for logged-in user or admins
    @DeleteMapping("/users/{username}/delete")
    @PreAuthorize("#username == authentication.name or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteByUsername(@PathVariable("username") String username) {

        try {
            userService.deleteByUsername(username);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(FORBIDDEN).build();
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }

}