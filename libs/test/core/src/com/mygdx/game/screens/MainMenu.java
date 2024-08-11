package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.LaserDoorsGame;

public class MainMenu implements Screen {
    private final LaserDoorsGame game;

    private final Texture buttonPlay = new Texture("buttons/buttonPlay.png");
    private final Texture buttonPlayDown = new Texture("buttons/buttonPlayDown.png");
    private final Texture buttonExit = new Texture("buttons/buttonExit.png");
    private final Texture buttonExitDown = new Texture("buttons/buttonExitDown.png");
    private final Texture buttonSettings = new Texture("buttons/buttonSettings.png");
    private final Texture buttonSettingsDown = new Texture("buttons/buttonSettingsDown.png");

    private final Texture laserDoors = new Texture("laserDoors.png");

    private final float laserDoorsX, laserDoorsY;
    private final float laserDoorsWidth, laserDoorsHeight;

    private final float screenWidth, screenHeight;

    private final SpriteBatch batch;

    private final float buttonPlayX, buttonPlayY;
    private final float buttonExitX, buttonExitY;
    private final float buttonSettingsX, buttonSettingsY;

    private final float btnPlayWidth, btnPlayHeight;
    private final float btnExitWidth, btnExitHeight;
    private final float btnSettingsWidth, btnSettingsHeight;

    /**
     * Game main menu.
     *
     * @param game game
     */
    public MainMenu(LaserDoorsGame game) {
        this.game = game;
        this.batch = new SpriteBatch();

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        this.btnPlayWidth = buttonPlay.getWidth();
        this.btnPlayHeight = buttonPlay.getHeight();

        this.btnExitWidth = buttonExit.getWidth();
        this.btnExitHeight = buttonExit.getHeight();

        this.btnSettingsWidth = 60;
        this.btnSettingsHeight = 60;

        this.laserDoorsWidth = 960;
        this.laserDoorsHeight = 540;

        this.laserDoorsX = screenWidth / 2 - laserDoorsWidth / 2;
        this.laserDoorsY = screenHeight / 2 - laserDoorsHeight / 2 + 350;

        this.buttonPlayX = screenWidth / 2 - btnPlayWidth / 2;
        this.buttonPlayY = screenHeight / 2 - btnPlayHeight / 2 + 50;

        this.buttonExitX = screenWidth / 2 - btnExitWidth / 2;
        this.buttonExitY = screenHeight / 2 - btnExitHeight / 2 - 50;

        this.buttonSettingsX = screenWidth / 2 - btnSettingsWidth / 2;
        this.buttonSettingsY = screenHeight / 2 - btnSettingsHeight / 2 - 150;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.batch.begin();
        this.batch.draw(laserDoors, laserDoorsX, laserDoorsY, laserDoorsWidth, laserDoorsHeight);
        this.startGame();
        this.exitGame();
        this.drawSettings();
        this.batch.end();
    }

    /**
     * Draw start button.
     */
    public void startGame() {
        if (this.isButtonPlay()) {
            this.batch.draw(buttonPlayDown, buttonPlayX, buttonPlayY, btnPlayWidth, btnPlayHeight);
            if (Gdx.input.isTouched()) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.game.getSoundSettings().playClickSound();
                game.setScreen(new Levels(game));
            }
        } else {
            this.batch.draw(buttonPlay, buttonPlayX, buttonPlayY, btnPlayWidth, btnPlayHeight);
        }
    }

    /**
     * Draw settings button.
     */
    public void drawSettings() {
        if (this.isButtonSettings()) {
            this.batch.draw(buttonSettingsDown, buttonSettingsX, buttonSettingsY, btnSettingsWidth, btnSettingsHeight);
            if (Gdx.input.isTouched()) {
                Settings settings = new Settings(this.game);
                settings.setPreviousScreen(this);
                this.game.getSoundSettings().playClickSound();
                this.game.setScreen(settings);
            }
        } else {
            this.batch.draw(buttonSettings, buttonSettingsX, buttonSettingsY, btnSettingsWidth, btnSettingsHeight);
        }
    }

    /**
     * Check if settings button is hovered.
     *
     * @return boolean
     */
    public boolean isButtonSettings() {
        return Gdx.input.getX() < buttonSettingsX + btnSettingsWidth
                && Gdx.input.getX() > buttonSettingsX
                && screenHeight - Gdx.input.getY() < buttonSettingsY + btnSettingsHeight
                && screenHeight - Gdx.input.getY() > buttonSettingsY;
    }

    /**
     * Draw exit button.
     */
    public void exitGame() {
        if (this.isButtonExit()) {
            this.batch.draw(buttonExitDown, buttonExitX, buttonExitY, btnExitWidth, btnExitHeight);
            if (Gdx.input.isTouched()) {
                this.game.getSoundSettings().playClickSound();
                System.exit(0);
            }
        } else {
            this.batch.draw(buttonExit, buttonExitX, buttonExitY, btnExitWidth, btnExitHeight);
        }
    }

    /**
     * Check if exit button is hovered.
     *
     * @return boolean
     */
    public boolean isButtonExit() {
        return Gdx.input.getX() < buttonExitX + btnExitWidth
                && Gdx.input.getX() > buttonExitX
                && screenHeight - Gdx.input.getY() < buttonExitY + btnExitHeight
                && screenHeight - Gdx.input.getY() > buttonExitY;
    }

    /**
     * Check if play button is hovered.
     *
     * @return boolean
     */
    public boolean isButtonPlay() {
        return Gdx.input.getX() < buttonPlayX + btnPlayWidth
                && Gdx.input.getX() > buttonPlayX
                && screenHeight - Gdx.input.getY() < buttonPlayY + btnPlayHeight
                && screenHeight - Gdx.input.getY() > buttonPlayY;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        buttonPlay.dispose();
        buttonPlayDown.dispose();
        buttonExit.dispose();
        buttonExitDown.dispose();
        laserDoors.dispose();
    }
}
