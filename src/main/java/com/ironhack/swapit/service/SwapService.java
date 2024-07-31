package com.ironhack.swapit.service;

import com.ironhack.swapit.dto.display.ItemDisplay;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.User;

import java.util.List;

public interface SwapService {

    // GET methods
    ItemDisplay findRandomItem(String username);

    // PUT methods
    void like(String username, int itemId);

}