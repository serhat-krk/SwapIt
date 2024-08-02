package com.ironhack.swapit.service;

import com.ironhack.swapit.dto.display.RandomItemDisplay;

public interface SwapService {

    // GET methods
    RandomItemDisplay findRandomItem(String username);

    // PUT methods
    void like(String username, int itemId);

}