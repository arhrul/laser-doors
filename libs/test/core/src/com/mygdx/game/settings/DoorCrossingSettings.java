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

    private boolean onRedDoor = false;
    private boolean onGreenDoor = false;

    public DoorCrossingSettings() {

    }

    /**
     * Allow movement after crossing the door.
     */
    public void allowMovement() {
        this.prohibitMovingUp = false;
        this.prohibitMovingDown = false;
        this.prohibitMovingLeft = false;
        this.prohibitMovingRight = false;
    }

    /**
     * Prohibit movement back if player touches the door.
     */
    public void prohibitMovementBack() {
        if (this.doorType == DoorTypes.HORIZONTAL && (this.currentDirection == Directions.UP
                || this.currentDirection == Directions.UP_LEFT
                || this.currentDirection == Directions.UP_RIGHT)) {
            this.prohibitMovingDown = true;
        } else if (this.doorType == DoorTypes.HORIZONTAL && (this.currentDirection == Directions.DOWN
                || this.currentDirection == Directions.DOWN_LEFT
                || this.currentDirection == Directions.DOWN_RIGHT)) {
            this.prohibitMovingUp = true;
        } else if (this.doorType == DoorTypes.VERTICAL && (this.currentDirection == Directions.LEFT
                || this.currentDirection == Directions.DOWN_LEFT
                || this.currentDirection == Directions.UP_LEFT)) {
            this.prohibitMovingRight = true;

        } else if (this.doorType == DoorTypes.VERTICAL && (this.currentDirection == Directions.RIGHT
                || this.currentDirection == Directions.DOWN_RIGHT
                || this.currentDirection == Directions.UP_RIGHT
        )) {
            this.prohibitMovingLeft = true;
        }
    }

    public void setOnRedDoor(boolean onRedDoor) {
        this.onRedDoor = onRedDoor;
    }

    public void setOnGreenDoor(boolean onGreenDoor) {
        this.onGreenDoor = onGreenDoor;
    }
}
