package com.ironhack.swapit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    // Properties
    @Id
    @GeneratedValue(strategy = UUID)
    private UUID userId;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String city;

    @ManyToMany(fetch = EAGER) // A user can like many items, an item can be liked by many users
    @JoinTable(
            name = "item_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> likedItems = new HashSet<>();

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles = new ArrayList<>();


    // Custom constructor without liked items
    public User(String username, String password, String firstName, String lastName, String city) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }


    // Methods

}
