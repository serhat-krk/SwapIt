package com.ironhack.swapit.repository;

import com.ironhack.swapit.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository <Item, UUID> {
}
