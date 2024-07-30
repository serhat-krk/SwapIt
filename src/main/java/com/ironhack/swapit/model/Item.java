package com.ironhack.swapit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Column(length = 120)
    @Size(max = 120,
            message = "Title length must be maximum 120 characters")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Column
    @Size(max = 255,
            message = "Description length must be maximum 255 characters")
    private String description;

    @ManyToOne // A user can have many items, an item has one user only
    private User owner;

    @ManyToMany(mappedBy = "likedItems", fetch = EAGER) // TODO: check if it works with LAZY
    @JsonIgnore
    private Collection<User> likedBy = new HashSet<>();


    // Custom Constructors
    public Item(String title, User owner) {
        this.title = title;
        this.owner = owner;
    }

    public Item(String title, String description, User owner) {
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

}