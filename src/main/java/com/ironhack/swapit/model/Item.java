package com.ironhack.swapit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.swapit.enums.ItemCondition;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static jakarta.persistence.InheritanceType.*;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = SINGLE_TABLE)
@Table(name = "items")
public abstract class Item {

    // Properties
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int itemId;

    @Column
    private String title;

    @Column
    private String description;

    @Enumerated(STRING)
    @Column(name = "item_condition")
    private ItemCondition condition;

    @ManyToOne // A user can have many items, an item has one user only
    private User owner;

    @ManyToMany(mappedBy = "likedItems", fetch = EAGER)
    private Collection<User> likedBy = new ArrayList<>();


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
