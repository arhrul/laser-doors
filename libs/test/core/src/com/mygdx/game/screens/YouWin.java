package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.LaserDoorsGame;

import java.util.Objects;


public class YouWin implements Screen {
    private final LaserDoorsGame game;
    private final SpriteBatch batch;

    private final Texture buttonNextLevel = new Texture("buttons/buttonNextLevel.png");
    private final Texture buttonNextLevelDown = new Texture("buttons/buttonNextLevelDown.png");

    private final Texture buttonMenu = new Texture("buttons/buttonMenu.png");
    private final Texture buttonMenuDown = new Texture("buttons/buttonMenuDown.png");

    private final Texture buttonRestart = new Texture("buttons/buttonRestart.png");
    private final Texture buttonRestartDown = new Texture("buttons/buttonRestartDown.png");

    private final float buttonRestartWidth;
    private final float buttonRestartHeight;
    private final float buttonRestartX;
    private final float buttonRestartY;

    private final float buttonNextLevelWidth;
    private final float buttonNextLevelHeight;
    private final float buttonNextLevelX;
    private final float buttonNextLevelY;

    private final float buttonMenuWidth;
    private final float buttonMenuHeight;
    private final float buttonMenuX;
    private final float buttonMenuY;

    private final Texture youWin = new Texture("youWin.png");

    private final float youWinX;
    private final float youWinY;
    private final float youWinWidth;
    private final float youWinHeight;

    private final Texture stars;
    private final float starsWidth;
    private final float starsHeight;

    private final float starsX;
    private final float starsY;

    private final float screenWidth, screenHeight;

    private final String levelPath;

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

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

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

    @Override
    public void show() {

    }

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
                this.game.getSoundSettings().playClickSound();
                this.game.getSkinSettings().resetColor();
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
        if (this.isButtonRestart() && !Objects.equals(levelPath, "LastLevel")) {
            this.batch.draw(buttonRestartDown, buttonRestartX, buttonRestartY);
            if (Gdx.input.isTouched()) {
                this.game.getSoundSettings().playClickSound();
                this.game.getSkinSettings().resetColor();
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
