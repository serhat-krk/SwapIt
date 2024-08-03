package com.ironhack.swapit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.swapit.dto.display.ItemDisplay;
import com.ironhack.swapit.dto.display.RandomItemDisplay;
import com.ironhack.swapit.dto.display.UserDisplay;
import com.ironhack.swapit.dto.request.LikeRequest;
import com.ironhack.swapit.enums.ItemClass;
import com.ironhack.swapit.model.Book;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.security.SecurityConfig;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.MatchService;
import com.ironhack.swapit.service.SwapService;
import com.ironhack.swapit.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.ironhack.swapit.enums.BookGenre.FANTASY;
import static com.ironhack.swapit.enums.ItemClass.BOOK;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SwapController.class)
@ExtendWith(SpringExtension.class)
@Import(SecurityConfig.class)
public class SwapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserService userService;

    @MockBean
    private SwapService swapService;

    @MockBean
    private MatchService matchService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "demouser1", roles = "USER")
    void getRandom_ShouldReturnRandomItemForLoggedInUser() throws Exception {
        // Arrange
        String username = "demouser1";
        RandomItemDisplay randomItemDisplay = new RandomItemDisplay(
                1,
                "Random Item",
                "Description of random item",
                BOOK
        );

        given(swapService.findRandomItem(username)).willReturn(randomItemDisplay);

        // Act & Assert
        mockMvc.perform(get("/api/swap"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.title").value("Random Item"))
                .andExpect(jsonPath("$.description").value("Description of random item"))
                .andExpect(jsonPath("$.itemClass").value(BOOK.toString()));
    }

    @Test
    @WithMockUser(username = "demouser1", roles = "USER")
    void like_ShouldAddItemToLikedItemsForLoggedInUser() throws Exception {
        // Arrange
        String username = "demouser1";
        User user = new User(username, "Password123", "email@demo.com", "User One", "City");
        User owner = new User("otheruser", "Password123", "otheruser@demo.com", "Other User", "City");

        Item likedItem = new Book("Title1", "Description1", owner, BOOK, "Author1", FANTASY);
        LikeRequest likeRequest = new LikeRequest();
        likeRequest.setItemId(likedItem.getItemId());

        given(itemService.findById(likeRequest.getItemId())).willReturn(Optional.of(likedItem));
        given(userService.findByUsername(username)).willReturn(user);

        doNothing().when(swapService).like(username, likedItem.getItemId());
        doNothing().when(matchService).createMatchIfMutualLike(user, owner);

        // Act & Assert
        mockMvc.perform(post("/api/swap/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(likeRequest)))
                .andExpect(status().isOk());

        verify(swapService, times(1)).like(username, likedItem.getItemId());
        verify(matchService, times(1)).createMatchIfMutualLike(user, owner);
    }

    @Test
    @WithMockUser(username = "demouser1", roles = "USER")
    void like_ShouldReturnForbiddenWhenLikingOwnItem() throws Exception {
        // Arrange
        String username = "demouser1";
        User owner = new User(username, "Password123", "email@demo.com", "User One", "City");

        Item likedItem = new Book("Title1", "Description1", owner, BOOK, "Author1", FANTASY);
        LikeRequest likeRequest = new LikeRequest();
        likeRequest.setItemId(likedItem.getItemId());

        given(itemService.findById(likeRequest.getItemId())).willReturn(Optional.of(likedItem));

        // Act & Assert
        mockMvc.perform(post("/api/swap/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(likeRequest)))
                .andExpect(status().isForbidden());

        verify(swapService, never()).like(username, likedItem.getItemId());
        verify(matchService, never()).createMatchIfMutualLike(any(), any());
    }
}