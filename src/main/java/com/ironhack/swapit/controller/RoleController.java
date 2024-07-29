package com.ironhack.swapit.controller;

import com.ironhack.swapit.dto.RoleToUser;
import com.ironhack.swapit.model.Role;
import com.ironhack.swapit.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    // Service Instantiation
    private final RoleService roleService;


// GET Mappings

    // Return all roles, for admins
    @GetMapping
    @Secured("ROLE_ADMIN")
    public List<Role> getAll() {
        return roleService.findAll();
    }


// POST Mappings

    // Add a role to a user, for admins
    @PostMapping("/add-to-user")
    @Secured("ROLE_ADMIN")
    public void addRoleToUser(@RequestBody RoleToUser roleToUser) {
        roleService.addRoleToUser(roleToUser.getUsername(), roleToUser.getRoleName());
    }

}
