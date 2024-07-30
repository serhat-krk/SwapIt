package com.ironhack.swapit.demo;

import com.ironhack.swapit.model.*;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.RoleService;
import com.ironhack.swapit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.ironhack.swapit.enums.BookGenre.*;
import static com.ironhack.swapit.enums.ClothingCategory.*;
import static com.ironhack.swapit.enums.ItemCondition.*;

@Component
@RequiredArgsConstructor
@Profile("load_demo_data")
public class DataLoader implements CommandLineRunner {

    // Service Instantiation
    private final ItemService itemService;
    private final UserService userService;
    private final RoleService roleService;


    @Override
    public void run(String... args) throws Exception {


        // Demo Roles
        roleService.save(new Role("ROLE_USER"));
        roleService.save(new Role("ROLE_ADMIN"));


        // Demo Users
        var demoUser1 = new User("demouser1", "Abc.1234", "demouser1@demo.com","Albert Smith", "Madrid");
        userService.save(demoUser1);
        var demoUser2 = new User("demouser2", "Abc.1234", "demouser2@demo.com", "Joe Cole", "London");
        userService.save(demoUser2);
        var demoUser3 = new User("demouser3", "Abc.1234", "demouser3@demo.com", "Hailey Strong", "Austin");
        userService.save(demoUser3);
        var demoUserAdmin = new User("demouseradmin", "Abc.1234", "demouseradmin@demo.com", "Chris River", "Berlin");
        userService.save(demoUserAdmin);

        // Add Admin Role
        roleService.addRoleToUser("demouseradmin", "ROLE_ADMIN");


        // Demo Items
        var demoItem1 = new Clothing(
                "Adidas Gazelle Men Shoes 43 Grey",
                "Only worn a few times. It's in good condition. It has a small yellow mark on the left side of the right foot.",
                USED,
                demoUser1,
                MEN,
                "43");
        itemService.save(demoItem1);

        var demoItem2 = new Clothing(
                "Levi's 501 Black Jeans W32 L34",
                USED,
                demoUser2,
                MEN,
                "W32 L34");
        itemService.save(demoItem2);

        var demoItem3 = new Book(
                "Game of Thrones",
                "I received as a gift and didn't open the cover",
                NOT_USED,
                demoUser3,
                "George R. R. Martin",
                FANTASY);
        itemService.save(demoItem3);

        var demoItem4 = new Book(
                "Sapiens",
                "No notes, no missing pages, in good condition",
                USED,
                demoUser3,
                "Yuval Noah Harari",
                HISTORY);
        itemService.save(demoItem4);

        var demoItem5 = new Book(
                "Hobbit",
                USED,
                demoUser3,
                "J. R. R. Tolkien",
                FANTASY);
        itemService.save(demoItem5);

        var demoItem6 = new Book(
                "Dune",
                NOT_USED,
                demoUser2,
                "Frank Herbert",
                SCIENCE_FICTION);
        itemService.save(demoItem5);

    }

}