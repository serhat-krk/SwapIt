package com.ironhack.swapit.model;

import com.ironhack.swapit.enums.ClothingCategory;
import com.ironhack.swapit.enums.ClothingType;
import com.ironhack.swapit.enums.ItemClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    @Column(name = "clothing_category")
    @Enumerated(STRING)
    private ClothingCategory category;

    @Column(name = "clothing_type")
    @Enumerated(STRING)
    private ClothingType type;

    @Column(name = "clothing_size", length = 10)
    @Size(min = 1, max = 10, message = "Size length must be minimum 1 and maximum 10 characters")
    private String size;


    // Custom Constructors
    public Clothing(String title, User owner, ItemClass itemClass, ClothingCategory category, ClothingType type, String size) {
        super(title, owner, itemClass);
        this.category = category;
        this.type = type;
        this.size = size;
    }

    public Clothing(String title, String description, User owner, ItemClass itemClass, ClothingCategory category, ClothingType type, String size) {
        super(title, description, owner, itemClass);
        this.category = category;
        this.type = type;
        this.size = size;
    }

}