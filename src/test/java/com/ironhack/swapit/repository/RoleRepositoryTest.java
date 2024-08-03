package com.ironhack.swapit.repository;

import com.ironhack.swapit.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    // Arrange
    @BeforeEach
    void setUp() {

        // Demo Roles
        roleRepository.save(new Role("ROLE_USER"));
        roleRepository.save(new Role("ROLE_ADMIN"));
    }

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
    }


// TESTS

    @Test
    void findByName() {

        // Act
        Role roleFound = roleRepository.findByName("ROLE_USER").orElseThrow();

        // Assert
        assertNotNull(roleFound);
        assertEquals("ROLE_USER", roleFound.getName());
    }
}