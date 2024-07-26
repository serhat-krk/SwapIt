package com.ironhack.swapit.model;

import com.ironhack.swapit.enums.ItemCondition;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.InheritanceType.*;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = JOINED)
@Table(name = "items")
public abstract class Item {

    // Properties
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID itemId;

    @Column
    private String title;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_condition")
    private ItemCondition condition;

    @ManyToOne // A user can have many items, an item has one user only
    private User owner;

    @ManyToMany(mappedBy = "likedItems")
    private Set<User> likes;


    // Custom Constructors
    public Item(String title, ItemCondition condition, User owner) {
        this.title = title;
        this.condition = condition;
        this.owner = owner;
    }

    public Item(String title, String description, ItemCondition condition, User owner) {
        this.title = title;
        this.description = description;
        this.condition = condition;
        this.owner = owner;
    }


    // Methods

}
