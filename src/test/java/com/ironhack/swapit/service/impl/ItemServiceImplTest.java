package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.dto.display.ItemDisplay;
import com.ironhack.swapit.dto.request.ItemRequest;
import com.ironhack.swapit.model.Book;
import com.ironhack.swapit.model.Clothing;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.ItemRepository;
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
import static com.ironhack.swapit.enums.ClothingCategory.WOMEN;
import static com.ironhack.swapit.enums.ClothingType.*;
import static com.ironhack.swapit.enums.ItemClass.BOOK;
import static com.ironhack.swapit.enums.ItemClass.CLOTHING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ItemServiceImpl itemService;

    private List<Item> demoItems;
    private List<User> demoUsers;

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
    void findAll() {

        // Arrange
        when(itemRepository.findAll()).thenReturn(demoItems);

        // Act
        List<Item> result = itemService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(demoItems.size(), result.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void findById() {

        // Arrange
        int id = 1;
        Item item = demoItems.get(0);
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        // Act
        Optional<Item> result = itemService.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(item, result.get());
        verify(itemRepository, times(1)).findById(id);
    }

    @Test
    void findUserItems() {

        // Arrange
        String username = "demouser3";
        List<Item> userItems = List.of(demoItems.get(2), demoItems.get(3), demoItems.get(4));
        when(itemRepository.findByOwner_Username(username)).thenReturn(userItems);

        // Act
        List<Item> result = itemService.findUserItems(username);

        // Assert
        assertNotNull(result);
        assertEquals(userItems.size(), result.size());
        verify(itemRepository, times(1)).findByOwner_Username(username);
    }

    @Test
    void save() {

        // Arrange
        Item item = new Clothing(
                "New Item",
                "Description of the new item",
                demoUsers.get(0), // Assuming Clothing is assigned to demoUser1
                CLOTHING, MEN, ACCESSORY, "L"
        );
        when(itemRepository.save(item)).thenReturn(item);

        // Act
        Item savedItem = itemService.save(item);

        // Assert
        assertNotNull(savedItem);
        assertEquals(item, savedItem);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void updateBook_UpdateAllFields() {

        // Arrange
        int itemId = 1;
        Book existingBook = new Book("Original Title", "Original Description", demoUsers.get(1), BOOK, "Original Author", FANTASY);
        ItemRequest updatedBook = new ItemRequest();
        updatedBook.setTitle("New Title");
        updatedBook.setDescription("New Description");
        updatedBook.setAuthor("New Author");
        updatedBook.setGenre(FANTASY); // Assuming same genre for simplicity

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingBook));
        when(itemRepository.save(existingBook)).thenReturn(existingBook);

        // Act
        itemService.updateBook(itemId, updatedBook);

        // Assert
        assertEquals("New Title", existingBook.getTitle());
        assertEquals("New Description", existingBook.getDescription());
        assertEquals("New Author", existingBook.getAuthor());
        assertEquals(FANTASY, existingBook.getGenre());
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(existingBook);
    }

    @Test
    void updateBook_UpdateSomeFields() {

        // Arrange
        int itemId = 2;
        Book existingBook = new Book("Original Title", "Original Description", demoUsers.get(2), BOOK, "Original Author", FANTASY);
        ItemRequest updatedBook = new ItemRequest();
        updatedBook.setTitle(null); // Title not updated
        updatedBook.setDescription("New Description");
        updatedBook.setAuthor("New Author");
        updatedBook.setGenre(null); // Genre not updated

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingBook));
        when(itemRepository.save(existingBook)).thenReturn(existingBook);

        // Act
        itemService.updateBook(itemId, updatedBook);

        // Assert
        assertEquals("Original Title", existingBook.getTitle());
        assertEquals("New Description", existingBook.getDescription());
        assertEquals("New Author", existingBook.getAuthor());
        assertEquals(FANTASY, existingBook.getGenre()); // Genre remains unchanged
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(existingBook);
    }

    @Test
    void updateBook_ItemNotFound() {

        // Arrange
        int itemId = 999; // Non-existent ID
        ItemRequest updatedBook = new ItemRequest();
        updatedBook.setTitle("New Title");
        updatedBook.setDescription("New Description");
        updatedBook.setAuthor("New Author");
        updatedBook.setGenre(FANTASY);

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.updateBook(itemId, updatedBook));
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void updateClothing_UpdateAllFields() {

        // Arrange
        int itemId = 1;
        Clothing existingClothing = new Clothing(
                "Original Title", "Original Description", demoUsers.get(0), CLOTHING, MEN, SHOES, "M"
        );
        ItemRequest updatedClothing = new ItemRequest();
        updatedClothing.setTitle("New Title");
        updatedClothing.setDescription("New Description");
        updatedClothing.setCategory(MEN);
        updatedClothing.setType(SHOES);
        updatedClothing.setSize("L");

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingClothing));
        when(itemRepository.save(existingClothing)).thenReturn(existingClothing);

        // Act
        itemService.updateClothing(itemId, updatedClothing);

        // Assert
        assertEquals("New Title", existingClothing.getTitle());
        assertEquals("New Description", existingClothing.getDescription());
        assertEquals(MEN, existingClothing.getCategory());
        assertEquals(SHOES, existingClothing.getType());
        assertEquals("L", existingClothing.getSize());
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(existingClothing);
    }

    @Test
    void updateClothing_UpdateSomeFields() {

        // Arrange
        int itemId = 2;
        Clothing existingClothing = new Clothing(
                "Original Title", "Original Description", demoUsers.get(1), CLOTHING, WOMEN, ACCESSORY, "S"
        );
        ItemRequest updatedClothing = new ItemRequest();
        updatedClothing.setDescription("New Description");
        updatedClothing.setSize("L");

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingClothing));
        when(itemRepository.save(existingClothing)).thenReturn(existingClothing);

        // Act
        itemService.updateClothing(itemId, updatedClothing);

        // Assert
        assertEquals("Original Title", existingClothing.getTitle());
        assertEquals("New Description", existingClothing.getDescription());
        assertEquals(WOMEN, existingClothing.getCategory()); // Category remains unchanged
        assertEquals(ACCESSORY, existingClothing.getType()); // Type remains unchanged
        assertEquals("L", existingClothing.getSize());
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(existingClothing);
    }

    @Test
    void updateClothing_ItemNotFound() {

        // Arrange
        int itemId = 999; // Non-existent ID
        ItemRequest updatedClothing = new ItemRequest();
        updatedClothing.setTitle("New Title");
        updatedClothing.setDescription("New Description");
        updatedClothing.setCategory(MEN);
        updatedClothing.setType(SHOES);
        updatedClothing.setSize("L");

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.updateClothing(itemId, updatedClothing));
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void deleteById_Success() {

        // Arrange
        int itemId = 1;
        Item existingItem = new Book(
                "Original Title", "Original Description", demoUsers.get(0), BOOK, "Original Author", FANTASY
        );

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(existingItem)).thenReturn(existingItem);

        // Act
        itemService.deleteById(itemId);

        // Assert
        assertNull(existingItem.getOwner());
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(existingItem);
        verify(itemRepository, times(1)).deleteById(itemId);
    }

    @Test
    void deleteById_ItemNotFound() {

        // Arrange
        int itemId = 999; // Non-existent ID
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.deleteById(itemId));
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, never()).save(any(Item.class));
        verify(itemRepository, never()).deleteById(itemId);
    }

    @Test
    void isOwner_UserIsOwner() {

        // Arrange
        int itemId = 1;
        String currentUsername = "existingUser";
        User owner = new User("existingUser", "Password123", "existing@demo.com", "Existing User", "City");
        Item item = new Book("Original Title", "Original Description", owner, BOOK, "Original Author", FANTASY);
        item.setItemId(itemId); // Assuming there's a setter or direct access to itemId

        // Mocking Security Context and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(currentUsername);

        // Mocking repository to return the item
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // Act
        boolean result = itemService.isOwner(itemId);

        // Assert
        assertTrue(result);
        verify(authentication, times(1)).getName();
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void isOwner_UserIsNotOwner() {

        // Arrange
        int itemId = 2;
        String currentUsername = "notOwner";
        User owner = new User("existingUser", "Password123", "existing@demo.com", "Existing User", "City");
        Item item = new Book("Original Title", "Original Description", owner, BOOK, "Original Author", FANTASY);
        item.setItemId(itemId); // Assuming there's a setter or direct access to itemId

        // Mocking Security Context and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(currentUsername);

        // Mocking repository to return the item
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // Act
        boolean result = itemService.isOwner(itemId);

        // Assert
        assertFalse(result);
        verify(authentication, times(1)).getName();
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void isOwner_ItemNotFound() {

        // Arrange
        int itemId = 999; // Non-existent itemId
        String currentUsername = "user";

        // Mocking Security Context and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(currentUsername);

        // Mocking repository to return empty Optional
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.isOwner(itemId));
        verify(authentication, times(1)).getName();
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void createDisplayItem_Book() {

        // Arrange
        User owner = new User("demouser1", "Abc.1234", "demouser1@demo.com", "Albert Smith", "Madrid");
        Book book = new Book("Game of Thrones", "I received as a gift and didn't open the cover", owner, BOOK, "George R. R. Martin", FANTASY);
        book.setItemId(1); // Assuming there's a setter or direct access to itemId

        // Act
        ItemDisplay itemDisplay = itemService.createDisplayItem(book);

        // Assert
        assertNotNull(itemDisplay);
        assertEquals(book.getItemId(), itemDisplay.getItemId());
        assertEquals(book.getTitle(), itemDisplay.getTitle());
        assertEquals(book.getDescription(), itemDisplay.getDescription());

        // Verify the UserDisplay properties
        assertNotNull(itemDisplay.getUser());
        assertEquals(owner.getName(), itemDisplay.getUser().getName());
        assertEquals(owner.getEmail(), itemDisplay.getUser().getEmail());
        assertEquals(owner.getCity(), itemDisplay.getUser().getCity());

        // Verify shared and specific properties
        assertEquals(book.getItemClass(), itemDisplay.getItemClass());
        assertEquals(book.getGenre(), itemDisplay.getGenre());
        assertEquals(book.getAuthor(), itemDisplay.getAuthor());
    }

    @Test
    void createDisplayItem_Clothing() {
        // Arrange
        User owner = new User("demouser2", "Abc.1234", "demouser2@demo.com", "Joe Cole", "London");
        Clothing clothing = new Clothing("Levi's 501 Black Jeans W32 L34", "Barely worn and in great condition", owner, CLOTHING, MEN, JEANS, "W32 L34");
        clothing.setItemId(2); // Assuming there's a setter or direct access to itemId

        // Act
        ItemDisplay itemDisplay = itemService.createDisplayItem(clothing);

        // Assert
        assertNotNull(itemDisplay);
        assertEquals(clothing.getItemId(), itemDisplay.getItemId());
        assertEquals(clothing.getTitle(), itemDisplay.getTitle());
        assertEquals(clothing.getDescription(), itemDisplay.getDescription());

        // Verify the UserDisplay properties
        assertNotNull(itemDisplay.getUser());
        assertEquals(owner.getName(), itemDisplay.getUser().getName());
        assertEquals(owner.getEmail(), itemDisplay.getUser().getEmail());
        assertEquals(owner.getCity(), itemDisplay.getUser().getCity());

        // Verify shared and specific properties
        assertEquals(clothing.getItemClass(), itemDisplay.getItemClass());
        assertEquals(clothing.getCategory(), itemDisplay.getCategory());
        assertEquals(clothing.getType(), itemDisplay.getType());
        assertEquals(clothing.getSize(), itemDisplay.getSize());
    }
}