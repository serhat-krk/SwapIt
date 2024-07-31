package com.ironhack.swapit.controller;

import com.ironhack.swapit.dto.display.MatchDisplay;
import com.ironhack.swapit.model.Match;
import com.ironhack.swapit.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public List<MatchDisplay> getAll() {

        // Fetch all matches
        List<Match> allMatches = matchService.findAll();

        // Initiate match display list
        List<MatchDisplay> allDisplayMatches = new ArrayList<>();

        // Convert match list to match display list
        for (Match match : allMatches) {
            allDisplayMatches.add(matchService.createDisplayMatch(match));
        }

        // return match display list
        return allDisplayMatches;

    }

    // Return a match by id, for logged-in user or admins
    @GetMapping("/id/{id}")
    @PreAuthorize("@matchServiceImpl.isItemOwner(#matchId) or hasRole('ROLE_ADMIN')")
    public MatchDisplay getById(@PathVariable("id") int matchId) {

        return matchService.createDisplayMatch(matchService.findById(matchId).orElseThrow());
    }

    // Return list off all matches of a user, for logged-in user and admins
    @GetMapping("/user/{username}")
    @PreAuthorize("#username == authentication.principal or hasRole('ROLE_ADMIN')")
    public List<MatchDisplay> getByUser(@PathVariable("username") String username) {

        // Fetch all matches of user
        List<Match> allMatches = matchService.findUserMatches(username);

        // Initiate match display list
        List<MatchDisplay> allDisplayMatches = new ArrayList<>();

        // Convert match list to match display list
        for (Match match : allMatches) {
            allDisplayMatches.add(matchService.createDisplayMatch(match));
        }

        // return match display list
        return allDisplayMatches;
    }


// DELETE Mapping

    // Delete a match, for owner (logged-in user) or admins
    @DeleteMapping("/id/{id}/unmatch")
    @PreAuthorize("@matchServiceImpl.isItemOwner(#matchId) or hasRole('ROLE_ADMIN')")
    public void unmatch(@PathVariable("id") int matchId) {
        matchService.deleteById(matchId);
    }

}
