package com.ironhack.swapit.dto.request;

import com.ironhack.swapit.enums.BookGenre;
import com.ironhack.swapit.enums.ClothingCategory;
import com.ironhack.swapit.enums.ClothingType;
import com.ironhack.swapit.enums.ItemClass;
import lombok.Data;

@Data
public class ItemRequest {

    // Item properties
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

}
