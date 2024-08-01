package com.ironhack.swapit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.swapit.dto.RoleToUser;
import com.ironhack.swapit.model.Role;
import com.ironhack.swapit.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = RoleController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllRoles_ShouldReturnRolesWithAdminRole() throws Exception {
        // Arrange
        List<Role> roles = List.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN"));
        given(roleService.findAll()).willReturn(roles);

        // Act & Assert
        mockMvc.perform(get("/api/roles/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("ROLE_USER"))
                .andExpect(jsonPath("$[1].name").value("ROLE_ADMIN"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllRoles_ShouldReturnForbiddenForNonAdmin() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/roles/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addRoleToUser_ShouldCallServiceWithAdminRole() throws Exception {
        // Arrange
        RoleToUser roleToUser = new RoleToUser();
        roleToUser.setUsername("demouser1");
        roleToUser.setRoleName("ROLE_USER");

        doNothing().when(roleService).addRoleToUser(roleToUser.getUsername(), roleToUser.getRoleName());

        // Act & Assert
        mockMvc.perform(post("/api/roles/add-to-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roleToUser)))
                .andExpect(status().isOk());

        verify(roleService, times(1)).addRoleToUser(roleToUser.getUsername(), roleToUser.getRoleName());
    }

    @Test
    @WithMockUser(roles = "USER")
    void addRoleToUser_ShouldReturnForbiddenForNonAdmin() throws Exception {
        // Arrange
        RoleToUser roleToUser = new RoleToUser();
        roleToUser.setUsername("demouser1");
        roleToUser.setRoleName("ROLE_USER");

        // Act & Assert
        mockMvc.perform(post("/api/roles/add-to-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roleToUser)))
                .andExpect(status().isForbidden());

        verify(roleService, never()).addRoleToUser(anyString(), anyString());
    }

}