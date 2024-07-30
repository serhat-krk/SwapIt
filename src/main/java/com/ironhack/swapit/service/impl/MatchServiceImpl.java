package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.Match;
import com.ironhack.swapit.model.User;
import com.ironhack.swapit.repository.MatchRepository;
import com.ironhack.swapit.repository.UserRepository;
import com.ironhack.swapit.service.MatchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchServiceImpl implements MatchService {

    // Repository Instantiation
    private final MatchRepository matchRepository;

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);
    private final UserRepository userRepository;


// GET Methods

    @Override
    public List<Match> findAll() {
        return matchRepository.findAll();
    }


// Other Methods

    @Override
    public void createMatchIfMutualLike(User user1, User user2) {

        // Bring liked items of both users
        Collection<Item> user1LikedItems = user1.getLikedItems();
        Collection<Item> user2LikedItems = user2.getLikedItems();

        logger.debug("{} liked items: {}", user1.getUsername(), user1LikedItems);
        logger.debug("{} liked items: {}", user2.getUsername(), user2LikedItems);

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
            if (mutualLikeFound) {
                break;
            }
        }

        // Save the match to database when there is mutual like
        if (mutualLikeFound) {
            matchRepository.save(new Match(user1LikedItem, user2LikedItem));
        }

    }

}