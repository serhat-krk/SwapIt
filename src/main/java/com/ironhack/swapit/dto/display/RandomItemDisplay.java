package com.ironhack.swapit.dto.display;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ironhack.swapit.enums.BookGenre;
import com.ironhack.swapit.enums.ClothingCategory;
import com.ironhack.swapit.enums.ClothingType;
import com.ironhack.swapit.enums.ItemClass;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@Data
@JsonInclude(NON_NULL)
public class RandomItemDisplay {

    // Item properties
    private int itemId;
    private String title;
    private String description;
    private ItemClass itemClass;

    // Book properties
    private BookGenre genre;
    private String author;

    // Clothing properties
    private ClothingCategory category;
    private ClothingType type;
    private String size;

    // Custom Constructor with shared properties
    public RandomItemDisplay(int itemId, String title, String description, ItemClass itemClass) {
        this.itemId = itemId;
        this.title = title;
        this.description = description;
        this.itemClass = itemClass;
    }
}
