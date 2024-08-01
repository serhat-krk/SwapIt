package com.ironhack.swapit.controller;

import com.ironhack.swapit.model.User;
import com.ironhack.swapit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("#username == authentication.principal or hasRole('ROLE_ADMIN')")
    public User getByUsername(@PathVariable("username") String username) {
        return userService.findByUsername(username);
    }


// POST Mappings

    // Register a new user, public endpoint
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User post(@RequestBody @Valid User user) {
        return userService.save(user);
    }


// PUT Mappings

    // Update all user information, for logged-in user
    @PutMapping("/users/{username}/update")
    @PreAuthorize("#username == authentication.principal")
    public User update(@PathVariable("username") String username, @RequestBody @Valid User updatedUser) {
        return userService.update(username, updatedUser);
    }


// PATCH Mappings

    // Update user city, for logged-in user
    @PatchMapping("/users/{username}/update-city")
    @PreAuthorize("#username == authentication.principal")
    public User updateCity(@PathVariable("username") String username, @RequestBody @Valid String newCity) {
        return userService.updateCity(username, newCity);
    }


// DELETE Mappings

    // TODO: HTTP 403 Error, user still in db, fix it
    // Delete user from database, for logged-in user or admins
    @DeleteMapping("/users/{username}/delete")
    @PreAuthorize("#username == authentication.principal or hasRole('ROLE_ADMIN')")
    public void deleteByUsername(@PathVariable("username") String username) {
        userService.deleteByUsername(username);
    }

}