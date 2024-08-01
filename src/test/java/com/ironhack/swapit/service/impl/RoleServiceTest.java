package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.model.Role;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.RoleRepository;
import com.ironhack.swapit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void findAll() {

        // Arrange
        List<Role> roles = List.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN"));
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        List<Role> result = roleService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void findByName() {

        // Arrange
        String roleName = "ROLE_USER";
        Role role = new Role(roleName);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));

        // Act
        Role result = roleService.findByName(roleName);

        // Assert
        assertNotNull(result);
        assertEquals(roleName, result.getName());
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    void save() {

        // Arrange
        Role role = new Role("ROLE_USER");
        when(roleRepository.save(role)).thenReturn(role);

        // Act
        Role savedRole = roleService.save(role);

        // Assert
        assertNotNull(savedRole);
        assertEquals("ROLE_USER", savedRole.getName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void addRoleToUser() {

        // Arrange
        String username = "existingUser";
        String roleName = "ROLE_USER";

        User user = new User("existingUser", "Password123", "existing@demo.com", "Existing User", "City");
        Role role = new Role(roleName);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);

        // Act
        roleService.addRoleToUser(username, roleName);

        // Assert
        assertTrue(user.getRoles().contains(role));
        verify(userRepository, times(1)).findByUsername(username);
        verify(roleRepository, times(1)).findByName(roleName);
        verify(userRepository, times(1)).save(user);
    }
}