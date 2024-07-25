package com.ironhack.swapit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    // Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany(mappedBy = "roles", fetch = LAZY)
    @JsonIgnore
    private Collection<User> users = new ArrayList<>();


    // Custom Constructors
    public Role(String name) {
        this.name = name;
    }
}
