package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.dto.display.ItemDisplay;
import com.ironhack.swapit.dto.display.UserDisplay;
import com.ironhack.swapit.model.Book;
import com.ironhack.swapit.model.Clothing;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.ItemRepository;
import com.ironhack.swapit.repository.MatchRepository;
import com.ironhack.swapit.repository.UserRepository;
import com.ironhack.swapit.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
public class SwapServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private SwapServiceImpl swapService;

    private List<User> demoUsers;
    private List<Item> demoItems;

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
    }

    @Test
    void findRandomItem() {

        // Arrange
        String username = "demouser1";
        User owner = new User("otheruser", "OtherPassword123", "otheruser@demo.com", "Other User", "Some City");
        Item randomItem = new Book("Some Book", "Some Description", owner, BOOK, "Some Author", FANTASY);
        randomItem.setItemId(1); // Assuming there's a setter or direct access to itemId

        ItemDisplay expectedDisplayItem = new ItemDisplay(
                randomItem.getItemId(),
                randomItem.getTitle(),
                randomItem.getDescription(),
                new UserDisplay(owner.getName(), owner.getCity(), owner.getEmail()),
                randomItem.getItemClass()
        );

        when(itemRepository.findRandomItem(username)).thenReturn(randomItem);
        when(itemService.createDisplayItem(randomItem)).thenReturn(expectedDisplayItem);

        // Act
        ItemDisplay displayItem = swapService.findRandomItem(username);

        // Assert
        assertNotNull(displayItem);
        assertEquals(expectedDisplayItem, displayItem);
        verify(itemRepository, times(1)).findRandomItem(username);
        verify(itemService, times(1)).createDisplayItem(randomItem);
    }

    @Test
    void like() {
        // Arrange
        String username = "demouser1";
        int itemId = 3;
        User user = new User("demouser1", "Abc.1234", "demouser1@demo.com", "Albert Smith", "Madrid");
        Item item = new Book("Game of Thrones", "I received as a gift and didn't open the cover", demoUsers.get(2), BOOK, "George R. R. Martin", FANTASY);
        item.setItemId(itemId); // Assuming there's a setter or direct access to itemId

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // Act
        swapService.like(username, itemId);

        // Assert
        assertTrue(user.getLikedItems().contains(item));
        verify(userRepository, times(1)).findByUsername(username);
        verify(itemRepository, times(1)).findById(itemId);
        verify(userRepository, times(1)).save(user);
    }
}