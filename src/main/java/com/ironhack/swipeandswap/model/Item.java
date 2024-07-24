package com.ironhack.swipeandswap.model;

import com.ironhack.swipeandswap.enums.ItemCategory;
import com.ironhack.swipeandswap.enums.ItemCondition;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "items")
public class Item {

    // properties
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID itemId;

    @Column
    private String title;

    @Column
    private String description;

    @Enumerated (EnumType.STRING)
    @Column(name = "item_category")
    private ItemCategory category;

    @Enumerated (EnumType.STRING)
    @Column(name = "item_condition")
    private ItemCondition condition;

    @ManyToOne // A user can have many items, an item has one user only
    private User owner;

    @ManyToMany(mappedBy = "likedItems")
    private Set<User> likes;


    // Custom Constructors
    public Item(String title, ItemCategory category, ItemCondition condition, User owner) {
        this.title = title;
        this.category = category;
        this.condition = condition;
        this.owner = owner;
    }

    public Item(String title, String description, ItemCategory category, ItemCondition condition, User owner) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.condition = condition;
        this.owner = owner;
    }


    // Methods

}
