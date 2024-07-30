package com.ironhack.swapit.repository;

import com.ironhack.swapit.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository <Item, Integer> {

    List<Item> findItemByOwner_Username(String username);

    @Query(value = "SELECT * " +
            "FROM swap_db.items i " +
            "JOIN swap_db.users u " +
            "ON i.owner_user_id = u.user_id " +
            "WHERE u.username <> :username " +
            "ORDER BY RAND() " +
            "LIMIT 1",
            nativeQuery = true)
    Item findRandomItem(@Param("username") String username);

}