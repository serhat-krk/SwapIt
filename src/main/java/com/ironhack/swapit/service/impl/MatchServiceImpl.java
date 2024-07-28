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

import java.util.List;
import java.util.Set;

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

        user1 = userRepository.findByUsername(user1.getUsername());
        user2 = userRepository.findByUsername(user2.getUsername());

        Set<Item> user1LikedItems = user1.getLikedItems();
        Set<Item> user2LikedItems = user2.getLikedItems();

        logger.debug("{} liked items: {}", user1.getUsername(), user1LikedItems);
        logger.debug("{} liked items: {}", user2.getUsername(), user2LikedItems);

        boolean mutualLikeFound = false;
        Item user1LikedItem = null;
        Item user2LikedItem = null;

        for (Item item2 : user1LikedItems) {
            logger.debug("Checking if item1 {} owned by {} matches User2 {}", item2.getItemId(), item2.getOwner().getUsername(), user2.getUsername());
            if (item2.getOwner().equals(user2)) {
                for (Item item1 : user2LikedItems) {
                    logger.debug("Checking if item2 {} owned by {} matches User1 {}", item1.getItemId(), item1.getOwner().getUsername(), user1.getUsername());
                    if (item1.getOwner().equals(user1)) {
                        mutualLikeFound = true;
                        user1LikedItem = item2;
                        user2LikedItem = item1;
                        break;
                    }
                }
            }

            if (mutualLikeFound) {
                break;
            }
        }

        if (mutualLikeFound) {
            logger.debug("Mutual like found: User1 liked item: {} | User2 liked item: {}", user1LikedItem.getItemId(), user2LikedItem.getItemId());
            Match match = new Match(user1LikedItem, user2LikedItem);
            matchRepository.save(match);
            logger.debug("Match saved: {} with items ({}, {})", match.getMatchId(), user1LikedItem.getItemId(), user2LikedItem.getItemId());
        } else {
            logger.debug("No mutual like found between users: {} and {}", user1.getUsername(), user2.getUsername());
        }

    }


}
