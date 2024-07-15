package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.LaserDoorsGame;

import java.util.Objects;


public class YouWin implements Screen {

    private LaserDoorsGame game;
    private SpriteBatch batch;

    private Texture buttonNextLevel = new Texture("buttons/buttonNextLevel.png");
    private Texture buttonNextLevelDown = new Texture("buttons/buttonNextLevelDown.png");

    private Texture buttonMenu = new Texture("buttons/buttonMenu.png");
    private Texture buttonMenuDown = new Texture("buttons/buttonMenuDown.png");

    private Texture buttonRestart = new Texture("buttons/buttonRestart.png");
    private Texture buttonRestartDown = new Texture("buttons/buttonRestartDown.png");

    private float buttonRestartWidth, buttonRestartHeight;
    private float buttonRestartX, buttonRestartY;

    private float buttonNextLevelWidth, buttonNextLevelHeight;
    private float buttonNextLevelX, buttonNextLevelY;

    private float buttonMenuWidth, buttonMenuHeight;
    private float buttonMenuX, buttonMenuY;

    private Texture youWin = new Texture("youWin.png");

    private float youWinX, youWinY;
    private float youWinWidth, youWinHeight;

    private Texture stars;
    private float starsWidth, starsHeight;

    private float starsX, starsY;

    private float screenWidth, screenHeight;

    private String levelPath;

    /**
     * You win screen constructor.
     *
     * @param game      the main game instance
     * @param starsPath the path to the stars texture
     * @param levelPath the path to the level
     */
    public YouWin(LaserDoorsGame game, String starsPath, String levelPath) {
        this.stars = new Texture(starsPath);
        this.game = game;

        this.levelPath = levelPath;

        this.batch = new SpriteBatch();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        this.starsWidth = 320;
        this.starsHeight = 100;

        this.youWinWidth = 480;
        this.youWinHeight = 270;

        this.starsX = screenWidth / 2 - starsWidth / 2;
        this.starsY = screenHeight / 2 - starsHeight / 2;

        this.youWinX = screenWidth / 2 - youWinWidth / 2;
        this.youWinY = screenHeight / 2 - youWinHeight / 2 + 150;

        this.buttonNextLevelWidth = buttonNextLevel.getWidth();
        this.buttonNextLevelHeight = buttonNextLevel.getHeight();
        this.buttonNextLevelX = screenWidth / 2 - buttonNextLevelWidth / 2;
        this.buttonNextLevelY = screenHeight / 2 - buttonNextLevelHeight / 2 - 150;

        this.buttonMenuWidth = buttonMenu.getWidth();
        this.buttonMenuHeight = buttonMenu.getHeight();
        this.buttonMenuX = screenWidth / 2 - buttonMenuWidth / 2;
        this.buttonMenuY = screenHeight / 2 - buttonMenuHeight / 2 - 350;

        this.buttonRestartWidth = buttonRestart.getWidth();
        this.buttonRestartHeight = buttonRestart.getHeight();
        this.buttonRestartX = screenWidth / 2 - buttonRestartWidth / 2;
        this.buttonRestartY = screenHeight / 2 - buttonRestartHeight / 2 - 250;
    }

    /**
     * Called when this screen becomes the current screen for a {@link LaserDoorsGame}.
     */
    @Override
    public void show() {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta the time in seconds since the last render
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.batch.begin();
        this.batch.draw(stars, starsX, starsY, starsWidth, starsHeight);
        this.batch.draw(youWin, youWinX, youWinY, youWinWidth, youWinHeight);
        this.runNextLevel();
        this.goToMenu();
        this.restartLevel();
        this.batch.end();
    }

    /**
     * Checks and handles the logic for proceeding to the next level.
     */
    public void runNextLevel() {
        if (this.isButtonNext()) {
            this.batch.draw(buttonNextLevelDown, buttonNextLevelX, buttonNextLevelY);
            if (Gdx.input.isTouched()) {
                game.setScreen(new GameScreen(game, levelPath));
            }
        } else {
            if (!Objects.equals(levelPath, "LastLevel")) {
                this.batch.draw(buttonNextLevel, buttonNextLevelX, buttonNextLevelY);
            }
        }
    }

    /**
     * Checks and handles the logic for restarting the current level.
     */
    public void restartLevel() {
        if (this.isButtonRestart()) {
            this.batch.draw(buttonRestartDown, buttonRestartX, buttonRestartY);
            if (Gdx.input.isTouched()) {
                game.setScreen(new GameScreen(game,
                        GameScreen.getLevels().get(GameScreen.getLevels().indexOf(levelPath) - 1)));
            }
        } else {
            if (!Objects.equals(levelPath, "LastLevel")) {
                this.batch.draw(buttonRestart, buttonRestartX, buttonRestartY);
            }
        }
    }

    /**
     * Checks if the restart button is pressed.
     *
     * @return {@code true} if the restart button is pressed, {@code false} otherwise
     */
    public boolean isButtonRestart() {
        return Gdx.input.getX() < buttonRestartX + buttonRestartWidth
                && Gdx.input.getX() > buttonRestartX
                && screenHeight - Gdx.input.getY() < buttonRestartY + buttonRestartHeight
                && screenHeight - Gdx.input.getY() > buttonRestartY;
    }

    /**
     * Checks if the next level button is pressed.
     *
     * @return {@code true} if the next level button is pressed, {@code false} otherwise
     */
    public boolean isButtonNext() {
        return Gdx.input.getX() < buttonNextLevelX + buttonNextLevelWidth
                && Gdx.input.getX() > buttonNextLevelX
                && screenHeight - Gdx.input.getY() < buttonNextLevelY + buttonNextLevelHeight
                && screenHeight - Gdx.input.getY() > buttonNextLevelY
                && !Objects.equals(levelPath, "LastLevel");
    }

    /**
     * Checks and handles the logic for going to the main menu.
     */
    public void goToMenu() {
        if (this.isButtonMenu()) {
            this.batch.draw(buttonMenuDown, buttonMenuX, buttonMenuY);
            if (Gdx.input.isTouched()) {
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
        buttonNextLevel.dispose();
        buttonNextLevelDown.dispose();
        buttonMenu.dispose();
        buttonMenuDown.dispose();
        buttonRestart.dispose();
        buttonRestartDown.dispose();
        stars.dispose();
        youWin.dispose();
    }
}
