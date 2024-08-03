package com.ironhack.swapit.service;

import com.ironhack.swapit.model.Role;

import java.util.List;

public interface RoleService {

    // GET Methods
    List<Role> findAll();
    Role findByName(String roleName);

    // POST Methods
    Role save(Role role);
    void addRoleToUser(String username, String roleName);

}