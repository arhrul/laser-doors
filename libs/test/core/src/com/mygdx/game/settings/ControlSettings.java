package com.mygdx.game.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;

public class ControlSettings {
    private String up;
    private String down;
    private String left;
    private String right;

    public ControlSettings() {
        this.up = "w";
        this.down = "s";
        this.left = "a";
        this.right = "d";
    }

    public void setControlByArrows() {
        this.up = Input.Keys.toString(Input.Keys.UP);
        this.down = Input.Keys.toString(Input.Keys.DOWN);
        this.left = Input.Keys.toString(Input.Keys.LEFT);
        this.right = Input.Keys.toString(Input.Keys.RIGHT);
    }

    public void setUp(String up) {
        this.up = up;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getUp() {
        return up;
    }

    public String getDown() {
        return down;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }
}
