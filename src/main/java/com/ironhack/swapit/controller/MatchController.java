package com.ironhack.swapit.controller;

import com.ironhack.swapit.model.Match;
import com.ironhack.swapit.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    // Service Instantiations
    private final MatchService matchService;


// GET Mappings

    // Return list off all matches, for admins
    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    public List<Match> getAll() {
        return matchService.findAll();
    }

    // Return a match by id, for logged-in user or admins
    @GetMapping("/id/{id}")
    @PreAuthorize("@matchServiceImpl.isItemOwner(#matchId) or hasRole('ROLE_ADMIN')")
    public Optional<Match> getById(@PathVariable("id") int matchId) {
        return matchService.findById(matchId);
    }

    // Return list off all matches of a user, for logged-in user and admins
    @GetMapping("/user/{username}")
    @PreAuthorize("#username == authentication.principal or hasRole('ROLE_ADMIN')")
    public List<Match> getByUser(@PathVariable("username") String username) {
        return matchService.findUserMatches(username);
    }

}
