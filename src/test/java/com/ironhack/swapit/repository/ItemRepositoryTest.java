package com.ironhack.swapit.repository;

import com.ironhack.swapit.model.Book;
import com.ironhack.swapit.model.Clothing;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.ironhack.swapit.enums.BookGenre.*;
import static com.ironhack.swapit.enums.ClothingCategory.MEN;
import static com.ironhack.swapit.enums.ClothingType.JEANS;
import static com.ironhack.swapit.enums.ClothingType.SHOES;
import static com.ironhack.swapit.enums.ItemClass.BOOK;
import static com.ironhack.swapit.enums.ItemClass.CLOTHING;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired private ItemRepository itemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private MatchRepository matchRepository;

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

        // Demo Items
        var demoItem1 = new Clothing(
                "Adidas Gazelle Men Shoes 43 Grey",
                "Only worn a few times. It's in good condition. It has a small yellow mark on the left side of the right foot.",
                demoUser1, CLOTHING, MEN, SHOES,"43");

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

        itemRepository.saveAll(List.of(demoItem1, demoItem2, demoItem3, demoItem4, demoItem5, demoItem6));
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }


// TESTS

    @Test
    void findByOwner_Username() {

        // Act
        List<Item> user3Items = itemRepository.findByOwner_Username("demouser3");
        List<Item> noItems = itemRepository.findByOwner_Username("no-user");

        // Assert
        assertEquals(3, user3Items.size());
        assertTrue(noItems.isEmpty());
    }

    @Test
    void findRandomItem() {

        // Running the test in a loop to repeat the check multiple times
        for (int i = 1; i < 100; i++) {

            // Act
            Item randomItem = itemRepository.findRandomItem("demouser1");

            // Assert
            assertNotEquals("demouser1", randomItem.getOwner().getUsername(), "Assertion failed on iteration: " + i);
        }
    }
}