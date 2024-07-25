package com.ironhack.swapit.service;

import com.ironhack.swapit.model.Role;

public interface RoleService {

    Role save(Role role);
    void addRoleToUser(String username, String roleName);

}
