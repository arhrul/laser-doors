package com.mygdx.game.settings;

import com.badlogic.gdx.graphics.Texture;

public class SkinSettings {
    private String currentSkinType;
    private String currentColor;

    private static final String standardSkin = "standard";

    public SkinSettings() {
        this.currentSkinType = standardSkin;
        this.currentColor = "Standard";
    }

    public void setCurrentColor(String currentColor) {
        this.currentColor = currentColor;
    }

    public String getCurrentSkinType() {
        return this.currentSkinType;
    }

    public void setCurrentSkinType(String currentSkinType) {
        this.currentSkinType = currentSkinType;
    }

    public void resetColor() {
        this.currentColor = "Standard";
    }

    public Texture getCurrentSkinTexture() {
        return new Texture("skins/player" + currentColor + "_" + currentSkinType + ".png");
    }

    public Texture getStandardCurrentSkin() {
        return new Texture("skins/playerStandard_" + this.currentSkinType + ".png");
    }

    public Texture getRedCurrentSkin() {
        return new Texture("skins/playerRed_" + this.currentSkinType + ".png");
    }

    public Texture getGreenCurrentSkin() {
        return new Texture("skins/playerGreen_" + this.currentSkinType + ".png");
    }
}
