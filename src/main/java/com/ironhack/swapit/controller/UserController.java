package com.ironhack.swapit.controller;

import com.ironhack.swapit.model.User;
import com.ironhack.swapit.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    // Service Instantiation
    private final UserService userService;


// GET Mappings

    // Return list of all users, for admins
    @GetMapping("/users")
    @Secured("ROLE_ADMIN")
    public List<User> getAll() {
        return userService.findAll();
    }

    // Return a user by username, for logged-in user or admins
    @GetMapping("/users/{username}")
    @PreAuthorize("#username == authentication.principal or hasRole('ROLE_ADMIN')")
    public User getByUsername(@PathVariable("username") String username) {
        return userService.findByUsername(username);
    }


// POST Mappings

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User post(@RequestBody @Valid User user) {
        return userService.save(user);
    }


// PUT Mappings


// PATCH Mappings


// Delete Mappings

}
