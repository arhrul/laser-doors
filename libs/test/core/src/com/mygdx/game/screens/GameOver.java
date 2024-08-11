package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.LaserDoorsGame;

public class GameOver implements Screen {
    private final LaserDoorsGame game;
    private final SpriteBatch batch;

    private final Texture buttonRestart = new Texture("buttons/buttonRestart.png");
    private final Texture buttonRestartDown = new Texture("buttons/ButtonRestartDown.png");

    private final Texture youLose = new Texture("youLose.png");

    private final Texture buttonMenu = new Texture("buttons/buttonMenu.png");
    private final Texture buttonMenuDown = new Texture("buttons/buttonMenuDown.png");

    private final float buttonRestartX;
    private final float buttonRestartY;
    private float btnRestartWidth, btnRestartHeight;

    private final float youLoseX;
    private final float youLoseY;
    private final float youLoseWidth;
    private final float youLoseHeight;

    private final float buttonMenuX;
    private final float buttonMenuY;
    private final float buttonMenuWidth;
    private final float buttonMenuHeight;

    private final float screenWidth, screenHeight;

    private final String levelPath;

    /**
     * Game over screen.
     *
     * @param game game
     */
    public GameOver(LaserDoorsGame game, String levelPath) {
        this.game = game;
        this.batch = new SpriteBatch();

        this.levelPath = levelPath;

        this.btnRestartWidth = buttonRestart.getWidth();
        this.btnRestartHeight = buttonRestart.getHeight();

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        this.buttonRestartX = screenWidth / 2 - btnRestartWidth / 2;
        this.buttonRestartY = screenHeight / 2 - btnRestartHeight / 2;

        this.youLoseWidth = 480;
        this.youLoseHeight = 270;

        this.youLoseX = screenWidth / 2 - youLoseWidth / 2;
        this.youLoseY = screenHeight / 2 - youLoseHeight / 2 + 150;

        this.btnRestartWidth = buttonRestart.getWidth();
        this.btnRestartHeight = buttonRestart.getHeight();

        this.buttonMenuWidth = buttonMenu.getWidth();
        this.buttonMenuHeight = buttonMenu.getHeight();

        this.buttonMenuX = screenWidth / 2 - buttonMenuWidth / 2;
        this.buttonMenuY = screenHeight / 2 - buttonMenuHeight / 2 - 100;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.batch.begin();
        this.restartGame();
        this.goToMenu();
        this.batch.draw(youLose, youLoseX, youLoseY, youLoseWidth, youLoseHeight);
        this.batch.end();
    }

    /**
     * Checks and handles the logic for restart.
     */
    public void restartGame() {
        if (this.isButtonRestart()) {
            this.batch.draw(buttonRestartDown, buttonRestartX, buttonRestartY);
            if (Gdx.input.isTouched()) {
                this.game.getSoundSettings().playClickSound();
                this.game.getSkinSettings().resetColor();
                game.setScreen(new GameScreen(game,
                        GameScreen.getLevels().get(GameScreen.getLevels().indexOf(levelPath) - 1)));
            }
        } else {
            this.batch.draw(buttonRestart, buttonRestartX, buttonRestartY);
        }
    }

    /**
     * Checks if the restart button is hovered.
     *
     * @return {@code true} if the restart button is pressed, {@code false} otherwise
     */
    public boolean isButtonRestart() {
        return Gdx.input.getX() < buttonRestartX + btnRestartWidth
                && Gdx.input.getX() > buttonRestartX
                && screenHeight - Gdx.input.getY() < buttonRestartY + btnRestartHeight
                && screenHeight - Gdx.input.getY() > buttonRestartY;
    }

    /**
     * Checks and handles the logic for going to the main menu.
     */
    public void goToMenu() {
        if (this.isButtonMenu()) {
            this.batch.draw(buttonMenuDown, buttonMenuX, buttonMenuY);
            if (Gdx.input.isTouched()) {
                this.game.getSoundSettings().playClickSound();
                this.game.getSkinSettings().resetColor();
                game.setScreen(new MainMenu(this.game));
            }
        } else {
            this.batch.draw(buttonMenu, buttonMenuX, buttonMenuY);
        }
    }

    /**
     * Checks if the menu button is hovered.
     *
     * @return {@code true} if the menu button is pressed, {@code false} otherwise
     */
    public boolean isButtonMenu() {
        return Gdx.input.getX() < buttonMenuX + buttonMenuWidth
                && Gdx.input.getX() > buttonMenuX
                && screenHeight - Gdx.input.getY() < buttonMenuY + buttonMenuHeight
                && screenHeight - Gdx.input.getY() > buttonMenuY;
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
        buttonRestart.dispose();
        buttonRestartDown.dispose();
        youLose.dispose();
        buttonMenu.dispose();
        buttonMenuDown.dispose();
    }
}
