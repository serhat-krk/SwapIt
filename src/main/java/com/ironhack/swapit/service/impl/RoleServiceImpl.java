package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.model.Role;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.RoleRepository;
import com.ironhack.swapit.repository.UserRepository;
import com.ironhack.swapit.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    // Repository Instantiations
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


// GET Methods

    @Override
    public List<Role> findAll() {
        log.info("Getting all roles");
        return roleRepository.findAll();
    }

    @Override
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName).orElseThrow();
    }


// POST Methods

    @Override
    public Role save(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);

        // Retrieve the user and role objects from the repository
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName).orElseThrow();

        // Add the role to the user's role collection
        user.getRoles().add(role);

        // Save the user to persist the changes
        userRepository.save(user);
    }

}
