package com.ironhack.swapit.service;

import com.ironhack.swapit.model.User;

import java.util.List;

public interface UserService {

    // GET methods
    List<User> findAll();
    List<User> findByCity(String city);
    User findByUsername(String username);

    // POST methods
    User save(User user);

    // PUT methods
    User update(String username, User user);

    // PATCH methods
    User updateCity(String username, String newCity);

    // DELETE methods
    void deleteByUsername(String username);

}