package com.ironhack.swipeandswap.demo;

import com.ironhack.swipeandswap.service.ItemService;
import com.ironhack.swipeandswap.service.UserService;
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

    }
}
