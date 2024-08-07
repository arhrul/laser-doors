package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.MainMenu;
import com.mygdx.game.screens.Settings;
import com.mygdx.game.settings.ControlSettings;
import com.mygdx.game.settings.SkinSettings;
import com.mygdx.game.settings.SoundSettings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LaserDoorsGame extends Game {
	private ControlSettings controlSettings;
	private SoundSettings soundSettings;
	private SkinSettings skinSettings;

	public LaserDoorsGame() {
	}

	@Override
	public void create () {
		this.setScreen(new MainMenu(this));
		this.controlSettings = new ControlSettings();
		this.soundSettings = new SoundSettings();
		this.skinSettings = new SkinSettings();
		this.soundSettings.playBgMusic();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}

	public ControlSettings getControlSettings() {
		return controlSettings;
	}

	public SoundSettings getSoundSettings() {
		return soundSettings;
	}

	public SkinSettings getSkinSettings() {
		return skinSettings;
	}
}
