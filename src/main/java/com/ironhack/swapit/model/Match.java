package com.ironhack.swapit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

/**
 * This model is to be used to match the items when 2 users like each other's items
 */

@Entity
@Data
@NoArgsConstructor
@Table(name = "matches")
public class Match {

    // Properties
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int matchId;

    @ManyToOne
    private Item item1;

    @ManyToOne
    private Item item2;


    // Custom Constructors
    public Match(Item item1, Item item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

}