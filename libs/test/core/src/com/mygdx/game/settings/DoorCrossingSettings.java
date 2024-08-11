package com.mygdx.game.settings;

import com.mygdx.game.enums.Directions;
import com.mygdx.game.enums.DoorTypes;

public class DoorCrossingSettings {
    private boolean prohibitMovingUp = false;
    private boolean prohibitMovingDown = false;
    private boolean prohibitMovingLeft = false;
    private boolean prohibitMovingRight = false;

    private DoorTypes doorType;
    private Directions currentDirection;

    public DoorCrossingSettings() {

    }
}
