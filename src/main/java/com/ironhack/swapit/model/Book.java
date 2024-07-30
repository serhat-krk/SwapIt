package com.ironhack.swapit.model;

import com.ironhack.swapit.enums.BookGenre;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @Column(name = "book_author")
    @NotBlank(message = "Author cannot be blank")
    private String author;

    @Column(name = "book_genre")
    @Enumerated(STRING)
    private BookGenre genre;


    // Custom Constructors
    public Book(String title, User owner, String author, BookGenre genre) {
        super(title, owner);
        this.author = author;
        this.genre = genre;
    }

    public Book(String title, String description, User owner, String author, BookGenre genre) {
        super(title, description, owner);
        this.author = author;
        this.genre = genre;
    }

}