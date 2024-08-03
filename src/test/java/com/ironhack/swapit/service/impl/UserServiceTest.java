package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.model.Book;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.Role;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.UserRepository;
import com.ironhack.swapit.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserServiceImpl userService;

    private List<User> userList;

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
        userList = Arrays.asList(demoUser1, demoUser2, demoUser3, demoUser5, demoUser6, demoUser7, demoUserAdmin);
    }


    @Test
    void findAll() {

        // Arrange
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> users = userService.findAll();

        // Assert
        assertNotNull(users);
        assertEquals(7, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findByCity() {

        // Arrange
        String city = "Madrid";
        List<User> madridUsers = Arrays.asList(userList.get(0), userList.get(3), userList.get(4));
        when(userRepository.findByCityIgnoreCase(city)).thenReturn(madridUsers);

        // Act
        List<User> users = userService.findByCity(city);

        // Assert
        assertNotNull(users);
        assertEquals(3, users.size());
        verify(userRepository, times(1)).findByCityIgnoreCase(city);
    }

    @Test
    void findByUsername_UserFound() {

        // Arrange
        String username = "demouser1";
        User demoUser1 = userList.get(0);
        when(userRepository.findByUsername(username)).thenReturn(demoUser1);

        // Act
        User user = userService.findByUsername(username);

        // Assert
        assertNotNull(user);
        assertEquals(demoUser1, user);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_UserNotFound() {

        // Arrange
        String username = "nonexistentUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.findByUsername(username)
        );

        assertEquals("User not found in the database", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void save() {

        // Arrange
        User user = new User("newuser", "PlainPassword123", "newuser@demo.com", "New User", "New York");
        String encodedPassword = "EncodedPassword123";
        Role roleUser = new Role("ROLE_USER");

        // Mocking the password encoding and role fetching
        when(passwordEncoder.encode("PlainPassword123")).thenReturn(encodedPassword);
        when(roleService.findByName("ROLE_USER")).thenReturn(roleUser);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.save(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(encodedPassword, savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(roleUser));
        verify(passwordEncoder, times(1)).encode("PlainPassword123");
        verify(roleService, times(1)).findByName("ROLE_USER");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void update() {

        // Arrange
        String username = "existingUser";
        User existingUser = new User("existingUser", "OldPassword123", "oldemail@demo.com", "Old User", "Old City");

        User updatedUser = new User("updatedUser", "NewPassword123", "newemail@demo.com", "New User", "New City");
        String encodedPassword = "EncodedNewPassword123";

        when(userRepository.findByUsername(username)).thenReturn(existingUser);
        when(passwordEncoder.encode("NewPassword123")).thenReturn(encodedPassword);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        User user = userService.update(username, updatedUser);

        // Assert
        assertNotNull(user);
        assertEquals("updatedUser", user.getUsername());
        assertEquals(encodedPassword, user.getPassword());
        assertEquals("newemail@demo.com", user.getEmail());
        assertEquals("New User", user.getName());
        assertEquals("New City", user.getCity());

        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).encode("NewPassword123");
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateCity() {

        // Arrange
        String username = "existingUser";
        String newCity = "New City";
        User existingUser = new User("existingUser", "Password123", "existing@demo.com", "Existing User", "Old City");

        when(userRepository.findByUsername(username)).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        User updatedUser = userService.updateCity(username, newCity);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(newCity, updatedUser.getCity());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void deleteByUsername_UserNotFound() {

        // Arrange
        String username = "nonexistentUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.deleteByUsername(username)
        );

        assertEquals("User not found in the database", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).deleteByUsername(username);
    }

    @Test
    void deleteByUsername_UserHasItems() {

        // Arrange
        String username = "userWithItems";
        User userWithItems = new User("userWithItems", "Password123", "user@demo.com", "User With Items", "City");

        // Create a Book instance to add to the user's ownedItems
        Item item = new Book(); // Assume Book is a subclass of Item
        userWithItems.getOwnedItems().add(item);

        when(userRepository.findByUsername(username)).thenReturn(userWithItems);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteByUsername(username)
        );

        assertEquals("You must delete all your items first", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).deleteByUsername(username);
    }

    @Test
    void deleteByUsername_Success() {

        // Arrange
        String username = "existingUser";
        User existingUser = new User("existingUser", "Password123", "existing@demo.com", "Existing User", "City");

        when(userRepository.findByUsername(username)).thenReturn(existingUser);
        doNothing().when(userRepository).deleteByUsername(username);

        // Act
        userService.deleteByUsername(username);

        // Assert
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).deleteByUsername(username);
    }
}