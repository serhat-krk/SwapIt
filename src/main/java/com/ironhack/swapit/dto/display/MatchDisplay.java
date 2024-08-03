package com.ironhack.swapit.dto.display;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchDisplay {

    private int matchId;
    private ItemDisplay item1;
    private ItemDisplay item2;

}
