package com.ironhack.swipeandswap.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
    private String name;

    @Column
    private String city;

    @ManyToMany
    private List<Item> likedItems;


    // Methods

}
