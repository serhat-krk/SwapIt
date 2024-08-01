package com.ironhack.swapit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = UserController.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAllUsers_ShouldReturnUsersWithAdminRole() throws Exception {
        // Arrange
        List<User> users = List.of(new User("demouser1", "password1", "email1@demo.com", "User One", "City1"),
                new User("demouser2", "password2", "email2@demo.com", "User Two", "City2"));
        given(userService.findAll()).willReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("demouser1"))
                .andExpect(jsonPath("$[1].username").value("demouser2"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllUsers_ShouldReturnForbiddenForNonAdmin() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isForbidden());
    }
}