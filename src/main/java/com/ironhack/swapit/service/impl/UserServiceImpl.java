package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.UserRepository;
import com.ironhack.swapit.service.RoleService;
import com.ironhack.swapit.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    // TODO: Find out what is the usage of this method
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve user with the given username
        User user = userRepository.findByUsername(username);
        // Check if user exists
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        else {
            log.info("User found in the database: {}", username);
            // Create a collection of SimpleGrantedAuthority objects from the user's roles
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            // Return the user details, including the username, password, and authorities
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }


// GET methods

    @Override
    public List<User> findAll() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public List<User> findByCity(String city) {
        log.info("Fetching all users from city: {}", city);
        return userRepository.findByCityIgnoreCase(city);
    }

    @Override
    public User findByUsername(String username) {

        // Retrieve user with the given username
        User user = userRepository.findByUsername(username);

        // Check if user exists
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }

        // Return user
        else {
            log.info("User found in the database: {}", username);
            return user;
        }
    }


// POST methods

    @Override
    public User save(User user) {
        log.info("Saving new user {} to the database", user.getUsername());

        // Encode the user's password for security before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Add ROLE_USER role before saving TODO: program fails
        user.getRoles().add(roleService.findByName("ROLE_USER"));

        // Save user to database
        return userRepository.save(user);
    }


// PUT methods

    @Override
    @Transactional
    public User update(String username, User updatedUser) {
        log.info("Updating user information: {}", username);

        // Find user in database
        User userToUpdate = userRepository.findByUsername(username);

        // Validate password before encoding
        

        // Update user with new info
        userToUpdate.setUsername(updatedUser.getUsername());
        userToUpdate.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        userToUpdate.setEmail(updatedUser.getEmail());
        userToUpdate.setName(updatedUser.getName());
        userToUpdate.setCity(updatedUser.getCity());

        // Save changes to database
        return userRepository.save(userToUpdate);
    }


// PATCH methods

    @Override
    @Transactional
    public User updateCity(String username, String newCity) {
        log.info("Updating user city: username {}, new city {}", username, newCity);

        // Find user in database
        User userToUpdate = userRepository.findByUsername(username);

        // Update user with new city
        userToUpdate.setCity(newCity);

        // Save changes to database
        return userRepository.save(userToUpdate);
    }


// DELETE methods

    @Override
    @Transactional
    public void deleteByUsername(String username) {

        // Retrieve user with the given username
        User user = userRepository.findByUsername(username);

        // Check if user exists
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }

        // Check if user has items
        else if (!user.getOwnedItems().isEmpty()){
            log.info("User found in the database: {}", username);
            throw new IllegalArgumentException("You must delete all your items first");
        }

        // Delete user
        else {
            userRepository.deleteByUsername(username);
            log.info("User deleted: {}", username);
        }
    }


}