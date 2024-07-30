package com.ironhack.swapit.repository;

import com.ironhack.swapit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository <User, UUID> {

    User findByUsername(String username);
    List<User> findByCityIgnoreCase(String city);
    void deleteByUsername(String username);

}
