package com.ironhack.swapit.repository;

import com.ironhack.swapit.model.Book;
import com.ironhack.swapit.model.Clothing;
import com.ironhack.swapit.model.Match;
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
public class MatchRepositoryTest {

    @Autowired private ItemRepository itemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private MatchRepository matchRepository;

    @BeforeEach
    void setUp() {

        // Demo Users
        var demoUser1 = new User("demouser1", "Abc.1234", "demouser1@demo.com", "Albert Smith", "Madrid");
        var demoUser2 = new User("demouser2", "Abc.1234", "demouser2@demo.com", "Joe Cole", "London");
        var demoUser3 = new User("demouser3", "Abc.1234", "demouser3@demo.com", "Hailey Strong", "Austin");
        var demoUser5 = new User("demouser5", "Abc.1234", "demouser5@demo.com", "Alfonso Lopez", "madrid");
        userRepository.saveAll(List.of(demoUser1, demoUser2, demoUser3, demoUser5));

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

        itemRepository.saveAll(List.of(demoItem1, demoItem2, demoItem3));

        // Demo Matches
        Match demoMatch1 = new Match(demoItem1, demoItem2);
        Match demoMatch2 = new Match(demoItem3, demoItem1);
        matchRepository.saveAll(List.of(demoMatch1, demoMatch2));
    }

    @AfterEach
    void tearDown() {
        matchRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByItem1_Owner_UsernameOrItem2_Owner_Username() {

        // Act
        List<Match> user1Matches = matchRepository.findByItem1_Owner_UsernameOrItem2_Owner_Username("demouser1", "demouser1");
        List<Match> user2Matches = matchRepository.findByItem1_Owner_UsernameOrItem2_Owner_Username("demouser2", "demouser2");
        List<Match> user5Matches = matchRepository.findByItem1_Owner_UsernameOrItem2_Owner_Username("demouser5", "demouser5");

        // Assert
        assertEquals(2, user1Matches.size());
        assertEquals(1, user2Matches.size());
        assertEquals(0, user5Matches.size());
    }
}