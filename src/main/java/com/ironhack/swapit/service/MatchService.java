package com.ironhack.swapit.service;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.Match;

import java.util.List;

public interface MatchService {

    // GET Methods
    List<Match> findAll();


    // POST Methods
    Match save(Match match);
}
