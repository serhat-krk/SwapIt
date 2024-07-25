package com.ironhack.swapit.service;

import com.ironhack.swapit.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    // GET methods
    List<User> findAll();

    Optional<User> findById(UUID id);


    // POST methods
    User save(User user);


    // PUT methods


    // PATCH methods


    // DELETE methods

}
