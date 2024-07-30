package com.ironhack.swapit.model;

import com.ironhack.swapit.enums.ClothingCategory;
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
public class Clothing extends Item {

    // Properties
    @Column
    @Enumerated(STRING)
    private ClothingCategory category;

    @Column
    private String size;


    // Custom Constructors
    public Clothing(String title, ItemCondition condition, User owner, ClothingCategory category, String size) {
        super(title, condition, owner);
        this.category = category;
        this.size = size;
    }

    public Clothing(String title, String description, ItemCondition condition, User owner, ClothingCategory category, String size) {
        super(title, description, condition, owner);
        this.category = category;
        this.size = size;
    }

}