package com.ironhack.swapit.dto;

import lombok.Data;

@Data
public class LikeRequest {

    private String username;
    private int itemId;

}
