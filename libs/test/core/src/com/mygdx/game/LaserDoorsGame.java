package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.MainMenu;
import com.mygdx.game.settings.ControlSettings;
import com.mygdx.game.settings.DoorCrossingSettings;
import com.mygdx.game.settings.SkinSettings;
import com.mygdx.game.settings.SoundSettings;

public class LaserDoorsGame extends Game {
    private ControlSettings controlSettings;
    private SoundSettings soundSettings;
    private SkinSettings skinSettings;
    private DoorCrossingSettings doorCrossingSettings;

    public LaserDoorsGame() {
    }

    @Override
    public void create() {
        this.setScreen(new MainMenu(this));
        this.controlSettings = new ControlSettings();
        this.soundSettings = new SoundSettings();
        this.skinSettings = new SkinSettings();
        this.doorCrossingSettings = new DoorCrossingSettings();
        this.soundSettings.playBgMusic();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }

    /**
     * Get control settings.
     *
     * @return control settings
     */
    public ControlSettings getControlSettings() {
        return controlSettings;
    }

    /**
     * Get sound settings.
     *
     * @return sound settings
     */
    public SoundSettings getSoundSettings() {
        return soundSettings;
    }

    /**
     * Get skin settings.
     *
     * @return skin settings
     */
    public SkinSettings getSkinSettings() {
        return skinSettings;
    }
}
