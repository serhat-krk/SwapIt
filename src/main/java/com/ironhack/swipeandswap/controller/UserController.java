package com.ironhack.swipeandswap.controller;

import com.ironhack.swipeandswap.model.User;
import com.ironhack.swipeandswap.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    // Service Instantiation
    private final UserService userService;


    // GET Mappings
    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getById(@PathVariable("id") UUID id) {
        return userService.findById(id);
    }


    // POST Mappings
    @PostMapping("/add")
    public User post(@RequestBody @Valid User user) {
        return userService.save(user);
    }


    // PUT Mappings


    // PATCH Mappings


    // Delete Mappings

}
