package com.ironhack.swapit.service;

import com.ironhack.swapit.dto.display.MatchDisplay;
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

    // POST Method
    void createMatchIfMutualLike(User user1, User user2);

    // DELETE Method
    void deleteById(int matchId);

    // Other Methods
    boolean isItemOwner(int matchId);
    MatchDisplay createDisplayMatch(Match match);

}