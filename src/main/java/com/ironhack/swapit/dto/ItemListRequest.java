package com.ironhack.swapit.dto;

import com.ironhack.swapit.enums.BookGenre;
import com.ironhack.swapit.enums.ClothingCategory;
import com.ironhack.swapit.enums.ClothingType;
import com.ironhack.swapit.enums.ItemClass;
import lombok.Data;

@Data
public class ItemListRequest {

    // Item properties
    private ItemClass itemClass;
    private String title;
    private String description;

    // Book properties
    private BookGenre genre;
    private String author;

    // Clothing properties
    private ClothingCategory category;
    private ClothingType type;
    private String size;

}
