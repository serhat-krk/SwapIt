package com.ironhack.swapit.service.impl;

import com.ironhack.swapit.model.Item;
import com.ironhack.swapit.model.Match;
import com.ironhack.swapit.repository.MatchRepository;
import com.ironhack.swapit.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchServiceImpl implements MatchService {

    // Repository Instantiation
    private final MatchRepository matchRepository;


// GET Methods

    @Override
    public List<Match> findAll() {
        return matchRepository.findAll();
    }

// POST Methods

    @Override
    public Match save(Match match) {
        return matchRepository.save(match);
    }

}
