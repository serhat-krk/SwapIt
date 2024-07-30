package com.ironhack.swapit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {

    // Properties
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column(name = "role_name")
    @Pattern(regexp = "^[A-Z_]*$") // TODO: To be tested
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Collection<User> users = new ArrayList<>();


    // Custom Constructor with name only
    public Role(String name) {
        this.name = name;
    }

}