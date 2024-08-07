package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
			ErrorLogger.logError("Uncaught exception in thread " + thread.getName(), throwable);
		});
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);

//		config.setWindowedMode(960, 640);
//		config.setResizable(false);

		config.setTitle("Laser Doors");
		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		config.setWindowIcon("skins/playerStandard_standard.png");
		new Lwjgl3Application(new LaserDoorsGame(), config);
	}
}
