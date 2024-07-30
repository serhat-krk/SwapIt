package com.ironhack.swapit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.*;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
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
    @Pattern(regexp = "^[a-zA-Z1-9]*$",
            message = "Username must only contain letters and digits")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Column
    // TODO: Application does not run when password validations are active. Might be due to encoding.
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&?!+=.,:;_-])",
//            message = "Password must contain at least: one small letter, one capital letter, one digit, one special character")
//    @Size(min = 8, max = 20,
//            message = "Password length must be minimum 8 and maximum 20 characters")
    private String password;

    @Column(unique = true)
    @Email(message = "Please provide a valid email address")
    private String email;

    @Column(name = "full_name")
    @Pattern(regexp = "^[a-zA-Z ]*$",
            message = "Name must only contain letters and blank space")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column
    @Pattern(regexp = "^[a-zA-Z ]*$",
            message = "City must only contain letters and blank space")
    @NotBlank(message = "City cannot be blank")
    private String city;

    @OneToMany(mappedBy = "owner", fetch = EAGER)
    private Collection<Item> ownedItems = new HashSet<>();

    @ManyToMany(fetch = EAGER) // A user can like many items, an item can be liked by many users
    @JoinTable(
            name = "item_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Collection<Item> likedItems = new HashSet<>();

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles = new HashSet<>();


    // Custom constructor without id, liked items or roles
    public User(String username, String password, String email, String name, String city) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.city = city;
    }

}