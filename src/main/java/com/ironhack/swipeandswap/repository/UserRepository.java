package com.ironhack.swipeandswap.repository;

import com.ironhack.swipeandswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository <User, UUID> {
}
