package com.ironhack.swapit.service;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.Match;
import com.ironhack.swapit.model.User;

import java.util.List;
import java.util.Optional;

public interface MatchService {

    // GET Methods
    List<Match> findAll();
    Optional<Match> findById(int matchId);
    List<Match> findUserMatches(String username);

    // POST Methods
    void createMatchIfMutualLike(User user1, User user2);

}