package com.ironhack.swapit.model;

import com.ironhack.swapit.enums.BookGenre;
import com.ironhack.swapit.enums.ItemCondition;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Book extends Item {

    // Properties
    @Column
    private String author;

    @Column
    @Enumerated(STRING)
    private BookGenre genre;


    // Custom Constructors
    public Book(String title, ItemCondition condition, User owner, String author, BookGenre genre) {
        super(title, condition, owner);
        this.author = author;
        this.genre = genre;
    }

    public Book(String title, String description, ItemCondition condition, User owner, String author, BookGenre genre) {
        super(title, description, condition, owner);
        this.author = author;
        this.genre = genre;
    }

}


