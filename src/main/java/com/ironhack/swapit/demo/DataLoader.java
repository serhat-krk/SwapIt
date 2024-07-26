package com.ironhack.swapit.demo;

import com.ironhack.swapit.enums.BookGenre;
import com.ironhack.swapit.enums.ClothingCategory;
import com.ironhack.swapit.enums.ItemCondition;
import com.ironhack.swapit.model.*;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.RoleService;
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
    private final RoleService roleService;


    @Override
    public void run(String... args) throws Exception {


        // Demo Roles
        roleService.save(new Role("ROLE_USER"));
        roleService.save(new Role("ROLE_ADMIN"));


        // Demo Users
        var demoUser1 = new User("demouser1", "1234", "Albert", "Smith", "Madrid");
        userService.save(demoUser1);
        var demoUser2 = new User("demouser2", "1234", "Joe", "Cole", "London");
        userService.save(demoUser2);
        var demoUser3 = new User("demouser3", "1234", "Hailey", "Strong", "Austin");
        userService.save(demoUser3);
        var demoUserAdmin = new User("demouseradmin", "1234", "Chris", "River", "Berlin");
        userService.save(demoUserAdmin);

        // Add Roles
        roleService.addRoleToUser("demouser1", "ROLE_USER");
        roleService.addRoleToUser("demouser2", "ROLE_USER");
        roleService.addRoleToUser("demouser3", "ROLE_USER");
        roleService.addRoleToUser("demouseradmin", "ROLE_ADMIN");


        // Demo Items
        var demoItem1 = new Clothing(
                "Adidas Gazelle Men Shoes 43 Grey",
                "Only worn a few times. It's in good condition. It has a small yellow mark on the left side of the right foot.",
                ItemCondition.USED,
                demoUser1,
                ClothingCategory.MEN,
                "43");
        itemService.save(demoItem1);

        var demoItem2 = new Clothing(
                "Levi's 501 Black Jeans W32 L34",
                ItemCondition.USED,
                demoUser2,
                ClothingCategory.MEN,
                "W32 L34");
        itemService.save(demoItem2);

        var demoItem3 = new Book(
                "Game of Thrones",
                "I received as a gift and didn't open the cover",
                ItemCondition.NOT_USED,
                demoUser3,
                "George R. R. Martin",
                BookGenre.FANTASY);
        itemService.save(demoItem3);

        var demoItem4 = new Book(
                "Sapiens",
                "No notes, no missing pages, in good condition",
                ItemCondition.USED,
                demoUser3,
                "Yuval Noah Harari",
                BookGenre.HISTORY);
        itemService.save(demoItem4);

    }
}
