package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.dto.display.ItemDisplay;
import com.ironhack.swapit.dto.display.MatchDisplay;
import com.ironhack.swapit.dto.display.UserDisplay;
import com.ironhack.swapit.model.*;
import com.ironhack.swapit.repository.MatchRepository;
import com.ironhack.swapit.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.ironhack.swapit.enums.BookGenre.*;
import static com.ironhack.swapit.enums.ClothingCategory.MEN;
import static com.ironhack.swapit.enums.ClothingType.JEANS;
import static com.ironhack.swapit.enums.ClothingType.SHOES;
import static com.ironhack.swapit.enums.ItemClass.BOOK;
import static com.ironhack.swapit.enums.ItemClass.CLOTHING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private MatchServiceImpl matchService;

    private List<User> demoUsers;
    private List<Item> demoItems;
    private List<Match> demoMatches;

    @BeforeEach
    void setUp() {
        // Demo Users
        var demoUser1 = new User("demouser1", "Abc.1234", "demouser1@demo.com", "Albert Smith", "Madrid");
        var demoUser2 = new User("demouser2", "Abc.1234", "demouser2@demo.com", "Joe Cole", "London");
        var demoUser3 = new User("demouser3", "Abc.1234", "demouser3@demo.com", "Hailey Strong", "Austin");
        var demoUser5 = new User("demouser5", "Abc.1234", "demouser5@demo.com", "Alfonso Lopez", "Madrid");
        var demoUser6 = new User("demouser6", "Abc.1234", "demouser6@demo.com", "Victor Herrera", "Madrid");
        var demoUser7 = new User("demouser7", "Abc.1234", "demouser7@demo.com", "Elizabeth", "LONDON");
        var demoUserAdmin = new User("demouseradmin", "Abc.1234", "demouseradmin@demo.com", "Chris River", "Berlin");

        demoUsers = List.of(demoUser1, demoUser2, demoUser3, demoUser5, demoUser6, demoUser7, demoUserAdmin);

        // Demo Items
        var demoItem1 = new Clothing(
                "Adidas Gazelle Men Shoes 43 Grey",
                "Only worn a few times. It's in good condition. It has a small yellow mark on the left side of the right foot.",
                demoUser1, CLOTHING, MEN, SHOES, "43");

        var demoItem2 = new Clothing(
                "Levi's 501 Black Jeans W32 L34",
                demoUser2, CLOTHING, MEN, JEANS, "W32 L34");

        var demoItem3 = new Book(
                "Game of Thrones",
                "I received as a gift and didn't open the cover",
                demoUser3, BOOK, "George R. R. Martin", FANTASY);

        var demoItem4 = new Book(
                "Sapiens",
                "No notes, no missing pages, in good condition",
                demoUser3, BOOK, "Yuval Noah Harari", HISTORY);

        var demoItem5 = new Book(
                "Hobbit",
                demoUser3, BOOK, "J. R. R. Tolkien", FANTASY);

        var demoItem6 = new Book(
                "Dune", demoUser2, BOOK, "Frank Herbert", SCIENCE_FICTION);

        demoItems = List.of(demoItem1, demoItem2, demoItem3, demoItem4, demoItem5, demoItem6);

        // Demo Matches
        Match demoMatch1 = new Match(demoItem1, demoItem2);
        Match demoMatch2 = new Match(demoItem3, demoItem1);
        Match demoMatch3 = new Match(demoItem4, demoItem5);
        Match demoMatch4 = new Match(demoItem6, demoItem3);
        Match demoMatch5 = new Match(demoItem2, demoItem6);

        demoMatches = List.of(demoMatch1, demoMatch2, demoMatch3, demoMatch4, demoMatch5);

    }

    @Test
    void findAll() {

        // Arrange
        when(matchRepository.findAll()).thenReturn(demoMatches);

        // Act
        List<Match> result = matchService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(demoMatches.size(), result.size());
        verify(matchRepository, times(1)).findAll();
    }

    @Test
    void findById() {

        // Arrange
        int matchId = 1;
        Match match = demoMatches.get(0);
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // Act
        Optional<Match> result = matchService.findById(matchId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(match, result.get());
        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    void findUserMatches() {

        // Arrange
        String username = "demouser1";

        // Assuming demoMatches contains matches that are relevant to 'demouser1'
        List<Match> expectedMatches = List.of(demoMatches.get(0), demoMatches.get(1));
        when(matchRepository.findByItem1_Owner_UsernameOrItem2_Owner_Username(username, username)).thenReturn(expectedMatches);

        // Act
        List<Match> result = matchService.findUserMatches(username);

        // Assert
        assertNotNull(result);
        assertEquals(expectedMatches.size(), result.size());
        assertTrue(result.containsAll(expectedMatches));
        verify(matchRepository, times(1)).findByItem1_Owner_UsernameOrItem2_Owner_Username(username, username);
    }

    @Test
    void createMatchIfMutualLike_MutualLikeExists() {

        // Arrange
        User user1 = demoUsers.get(0); // "demouser1"
        User user2 = demoUsers.get(1); // "demouser2"

        Item user1LikedItem = demoItems.get(1); // Item owned by demoUser2
        Item user2LikedItem = demoItems.get(0); // Item owned by demoUser1

        user1.getLikedItems().add(user1LikedItem);
        user2.getLikedItems().add(user2LikedItem);

        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        matchService.createMatchIfMutualLike(user1, user2);

        // Assert
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    void createMatchIfMutualLike_NoMutualLike() {

        // Arrange
        User user1 = demoUsers.get(0); // "demouser1"
        User user2 = demoUsers.get(1); // "demouser2"

        Item user1LikedItem = demoItems.get(2); // Item not owned by demoUser2
        Item user2LikedItem = demoItems.get(3); // Item not owned by demoUser1

        user1.getLikedItems().add(user1LikedItem);
        user2.getLikedItems().add(user2LikedItem);

        // Act
        matchService.createMatchIfMutualLike(user1, user2);

        // Assert
        verify(matchRepository, never()).save(any(Match.class));
    }

    @Test
    void deleteById() {

        // Arrange
        int matchId = 1;

        // Act
        matchService.deleteById(matchId);

        // Assert
        verify(matchRepository, times(1)).deleteById(matchId);
    }

    @Test
    void isItemOwner_UserIsOwnerOfItem1() {
        // Arrange
        int matchId = 1;
        String currentUsername = "demouser1";
        User owner1 = demoUsers.get(0); // "demouser1"
        User owner2 = demoUsers.get(1); // "demouser2"
        Item item1 = demoItems.get(0); // Owned by "demouser1"
        Item item2 = demoItems.get(1); // Owned by "demouser2"

        Match match = new Match(item1, item2);

        // Mocking Security Context and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(currentUsername);

        // Mocking repository to return the match with the desired matchId
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // Act
        boolean result = matchService.isItemOwner(matchId);

        // Assert
        assertTrue(result);
        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    void isItemOwner_UserIsOwnerOfItem2() {
        // Arrange
        int matchId = 2;
        String currentUsername = "demouser2";
        User owner1 = demoUsers.get(0); // "demouser1"
        User owner2 = demoUsers.get(1); // "demouser2"
        Item item1 = demoItems.get(0); // Owned by "demouser1"
        Item item2 = demoItems.get(1); // Owned by "demouser2"

        Match match = new Match(item1, item2);

        // Mocking Security Context and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(currentUsername);

        // Mocking repository to return the match with the desired matchId
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // Act
        boolean result = matchService.isItemOwner(matchId);

        // Assert
        assertTrue(result);
        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    void isItemOwner_UserIsNotOwnerOfEitherItem() {
        // Arrange
        int matchId = 3;
        String currentUsername = "otheruser";
        User owner1 = demoUsers.get(0); // "demouser1"
        User owner2 = demoUsers.get(1); // "demouser2"
        Item item1 = demoItems.get(0); // Owned by "demouser1"
        Item item2 = demoItems.get(1); // Owned by "demouser2"

        Match match = new Match(item1, item2);

        // Mocking Security Context and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(currentUsername);

        // Mocking repository to return the match with the desired matchId
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // Act
        boolean result = matchService.isItemOwner(matchId);

        // Assert
        assertFalse(result);
        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    void isItemOwner_MatchNotFound() {
        // Arrange
        int matchId = 999; // Non-existent matchId
        String currentUsername = "demouser1";

        // Mocking Security Context and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(currentUsername);

        // Mocking repository to return empty Optional
        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> matchService.isItemOwner(matchId));
        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    void createDisplayMatch() {
        // Arrange
        int matchId = 1;
        User owner1 = demoUsers.get(0); // "demouser1"
        User owner2 = demoUsers.get(1); // "demouser2"
        Item item1 = demoItems.get(0); // Owned by "demouser1"
        Item item2 = demoItems.get(1); // Owned by "demouser2"

        Match match = new Match(item1, item2);
        match.setMatchId(matchId); // Automatically generated, assume matchId for testing purposes

        ItemDisplay itemDisplay1 = new ItemDisplay(item1.getItemId(), item1.getTitle(), item1.getDescription(), new UserDisplay(owner1.getName(), owner1.getCity(), owner1.getEmail()), item1.getItemClass());
        ItemDisplay itemDisplay2 = new ItemDisplay(item2.getItemId(), item2.getTitle(), item2.getDescription(), new UserDisplay(owner2.getName(), owner2.getCity(), owner2.getEmail()), item2.getItemClass());

        when(itemService.createDisplayItem(item1)).thenReturn(itemDisplay1);
        when(itemService.createDisplayItem(item2)).thenReturn(itemDisplay2);

        MatchDisplay expectedDisplayMatch = new MatchDisplay(matchId, itemDisplay1, itemDisplay2);

        // Act
        MatchDisplay result = matchService.createDisplayMatch(match);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDisplayMatch.getMatchId(), result.getMatchId());
        assertEquals(expectedDisplayMatch.getItem1(), result.getItem1());
        assertEquals(expectedDisplayMatch.getItem2(), result.getItem2());
        verify(itemService, times(1)).createDisplayItem(item1);
        verify(itemService, times(1)).createDisplayItem(item2);
    }

}