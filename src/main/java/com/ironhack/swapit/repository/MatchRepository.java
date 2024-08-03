package com.ironhack.swapit.repository;

import com.ironhack.swapit.model.Match;
import com.ironhack.swapit.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {

    List<Match> findByItem1_Owner_UsernameOrItem2_Owner_Username(String item1_owner_username, String item2_owner_username);

}