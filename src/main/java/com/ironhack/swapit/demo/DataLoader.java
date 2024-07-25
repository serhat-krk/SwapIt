package com.ironhack.swapit.demo;

import com.ironhack.swapit.enums.ItemCategory;
import com.ironhack.swapit.enums.ItemCondition;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("load_demo_data")
public class DataLoader implements CommandLineRunner {

    // Service Instantiation
    private final ItemService itemService;
    private final UserService userService;


    @Override
    public void run(String... args) throws Exception {


        // Demo Users
        var demoUser1 = new User("demouser1", "1234", "Albert", "Smith", "Madrid");
        userService.save(demoUser1);
        var demoUser2 = new User("demouser2", "1234", "Joe", "Cole", "London");
        userService.save(demoUser2);
        var demoUser3 = new User("demouser3", "1234", "Haley", "Strong", "Austin");
        userService.save(demoUser3);


        // Demo Items
        var demoItem1 = new Item(
                "Adidas Gazelle Men Shoes 43 Grey",
                "Only worn a few times. It's in good condition. It has a small yellow mark on the left side of the right foot.",
                ItemCategory.CLOTHES,
                ItemCondition.USED,
                demoUser1);
        itemService.save(demoItem1);

        var demoItem2 = new Item(
                "Ikea MARKUS Black Office Chair",
                ItemCategory.CLOTHES,
                ItemCondition.USED,
                demoUser2);
        itemService.save(demoItem2);

        var demoItem3 = new Item(
                "Sony WF-C500 Earphones White UNOPENED",
                "I received as a gift and didn't open the box",
                ItemCategory.ELECTRONICS,
                ItemCondition.NOT_USED,
                demoUser3);
        itemService.save(demoItem3);

        var demoItem4 = new Item(
                "Sapiens - Yuval Noah Harari",
                "No notes, no missing pages, in good condition",
                ItemCategory.BOOKS,
                ItemCondition.USED,
                demoUser3);
        itemService.save(demoItem4);

    }
}
