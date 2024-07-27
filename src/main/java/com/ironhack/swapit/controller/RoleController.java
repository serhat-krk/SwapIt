package com.ironhack.swapit.controller;

import com.ironhack.swapit.dto.RoleToUser;
import com.ironhack.swapit.model.Role;
import com.ironhack.swapit.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    // Service Instantiation
    private final RoleService roleService;


    // GET Mappings
    @GetMapping
    public List<Role> getAll() {
        return roleService.findAll();
    }


    // POST Mappings
    @PostMapping("/add")
    public Role saveRole(@RequestBody Role role) {
        return roleService.save(role);
    }

    @PostMapping("/add-to-user")
    public void addRoleToUser(@RequestBody RoleToUser roleToUser) {
        roleService.addRoleToUser(roleToUser.getUsername(), roleToUser.getRoleName());
    }



}
