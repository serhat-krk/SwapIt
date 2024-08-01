package com.ironhack.swapit.repository;

import com.ironhack.swapit.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    // Arrange
    @BeforeEach
    void setUp() {

        // Demo Users
        var demoUser1 = new User("demouser1", "Abc.1234", "demouser1@demo.com", "Albert Smith", "Madrid");
        var demoUser2 = new User("demouser2", "Abc.1234", "demouser2@demo.com", "Joe Cole", "London");
        var demoUser3 = new User("demouser3", "Abc.1234", "demouser3@demo.com", "Hailey Strong", "Austin");
        var demoUser5 = new User("demouser5", "Abc.1234", "demouser5@demo.com", "Alfonso Lopez", "madrid");
        var demoUser6 = new User("demouser6", "Abc.1234", "demouser6@demo.com", "Victor Herrera", "Madrid");
        var demoUser7 = new User("demouser7", "Abc.1234", "demouser7@demo.com", "Elizabeth", "LONDON");
        var demoUserAdmin = new User("demouseradmin", "Abc.1234", "demouseradmin@demo.com", "Chris River", "Berlin");
        userRepository.saveAll(List.of(demoUser1, demoUser2, demoUser3, demoUser5, demoUser6, demoUser7, demoUserAdmin));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


// TESTS

    @Test
    void findByUsername() {

        // Act
        User user1 = userRepository.findByUsername("demouser1");
        User userNull = userRepository.findByUsername("null-user");

        // Assert
        assertNotNull(user1);
        assertEquals("demouser1", user1.getUsername());
        assertNull(userNull);
    }

    @Test
    void findByCityIgnoreCase() {

        // Act
        List<User> userListMadrid1 = userRepository.findByCityIgnoreCase("madrid");
        List<User> userListMadrid2 = userRepository.findByCityIgnoreCase("MaDriD");
        List<User> userListMadrid3 = userRepository.findByCityIgnoreCase("  MADRID  ");
        List<User> userListEmpty = userRepository.findByCityIgnoreCase("istanbul");

        // Assert
        assertEquals(3, userListMadrid1.size());
        assertEquals(3, userListMadrid2.size());
        assertNotEquals(3, userListMadrid3.size());
        assertEquals(0, userListEmpty.size());
    }

    @Test
    void deleteByUsername() {

        // Arrange
        List<User> allUsersBefore = userRepository.findAll();

        // Act
        userRepository.deleteByUsername("demouser1");
        List<User> allUsersAfter = userRepository.findAll();
        User userDeleted = userRepository.findByUsername("demouser1");

        // Assert
        assertEquals(1, allUsersBefore.size() - allUsersAfter.size());
        assertNull(userDeleted);
    }
}