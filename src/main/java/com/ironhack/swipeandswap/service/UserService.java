package com.ironhack.swipeandswap.service;

import com.ironhack.swipeandswap.model.User;
import com.ironhack.swipeandswap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    // Repository Instantiation
    private final UserRepository userRepository;


    // GET methods
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }


    // POST methods
    public User save(User user) {
        return userRepository.save(user);
    }


    // PUT methods


    // PATCH methods


    // DELETE methods

}
