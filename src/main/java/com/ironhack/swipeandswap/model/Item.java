package com.ironhack.swipeandswap.model;

import com.ironhack.swipeandswap.enums.ItemCategory;
import com.ironhack.swipeandswap.enums.ItemCondition;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table (name = "items")
public class Item {

    // properties

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    @Column (name = "item_id")
    private UUID id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private ItemCategory category;

    @NotNull
    private ItemCondition condition;

    @ManyToOne // A user can have many items, an item has one user only
    @NotNull
    private User owner;


}
