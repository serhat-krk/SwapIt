package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.dto.display.ItemDisplay;
import com.ironhack.swapit.dto.display.MatchDisplay;
import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.Match;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.MatchRepository;
import com.ironhack.swapit.repository.UserRepository;
import com.ironhack.swapit.service.ItemService;
import com.ironhack.swapit.service.MatchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchServiceImpl implements MatchService {

    // Repository Instantiation
    private final MatchRepository matchRepository;
    private final ItemService itemService;


// GET Methods

    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    public Optional<Match> findById(int matchId) {
        return matchRepository.findById(matchId);
    }

    public List<Match> findUserMatches(String username) {
        return matchRepository.findByItem1_Owner_UsernameOrItem2_Owner_Username(username, username);
    }


// POST Method

    @Transactional
    public void createMatchIfMutualLike(User user1, User user2) {

        // Bring liked items of both users
        Collection<Item> user1LikedItems = user1.getLikedItems();
        Collection<Item> user2LikedItems = user2.getLikedItems();

        // Create variables to use in the mutual likes loop
        boolean mutualLikeFound = false;
        Item user1LikedItem = null;
        Item user2LikedItem = null;

        // Double loop to check if user1 liked any item of user2, and vice versa
        for (Item item2 : user1LikedItems) {
            if (item2.getOwner().equals(user2)) {
                for (Item item1 : user2LikedItems) {
                    if (item1.getOwner().equals(user1)) {

                        // Update variables and break the inner loop when there is mutual like
                        mutualLikeFound = true;
                        user1LikedItem = item2;
                        user2LikedItem = item1;
                        break;
                    }
                }
            }

            // Break the outer loop when there is mutual like
            if (mutualLikeFound)
                break;
        }

        // Save the match to database when there is mutual like
        if (mutualLikeFound)
            matchRepository.save(new Match(user1LikedItem, user2LikedItem));

    }


// DELETE Method

    @Transactional
    public void deleteById(int matchId) {
        matchRepository.deleteById(matchId);
    }


// Other Methods

    /**
     * Security check if logged-in user owns the matched item
     * @return True: when user is owner
     *         False: when user is not owner
     */
    public boolean isItemOwner(int matchId) {

        // Find username of logged-in user
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find match by matchId
        Match match = matchRepository.findById(matchId).orElseThrow();

        // Check if current user is owner of either item in the match
        return match.getItem1().getOwner().getUsername().equals(currentUsername) ||
                match.getItem2().getOwner().getUsername().equals(currentUsername);
    }

    // Create display match
    public MatchDisplay createDisplayMatch(Match match) {

        return new MatchDisplay(
                match.getMatchId(),
                itemService.createDisplayItem(match.getItem1()),
                itemService.createDisplayItem(match.getItem2())
        );

    }

}