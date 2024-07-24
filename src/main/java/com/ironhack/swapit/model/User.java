package com.ironhack.swapit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    // Properties
    @Id
    @GeneratedValue
    private UUID userId;

    @Column
    private String username;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String city;

    @ManyToMany // A user can like many items, an item can be liked by many users
    @JoinTable(
            name = "item_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> likedItems;


    // Custom constructor without liked items
    public User(String username, String firstName, String lastName, String city) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }


    // Methods

}
