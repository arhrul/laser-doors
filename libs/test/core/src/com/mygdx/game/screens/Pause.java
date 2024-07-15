package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.LaserDoorsGame;

public class Pause implements Screen {
    private LaserDoorsGame game;
    private GameScreen gameScreen;

    private SpriteBatch batch;

    private Texture buttonContinue = new Texture("buttons/buttonContinue.png");
    private Texture buttonContinueDown = new Texture("buttons/buttonContinueDown.png");

    private Texture buttonExit = new Texture("buttons/buttonExit.png");
    private Texture buttonExitDown = new Texture("buttons/buttonExitDown.png");

    private Texture buttonMenu = new Texture("buttons/buttonMenu.png");
    private Texture buttonMenuDown = new Texture("buttons/buttonMenuDown.png");

    private float buttonMenuX, buttonMenuY;
    private float buttonMenuWidth, buttonMenuHeight;

    private float buttonContinueX, buttonContinueY;
    private float btnContinueWidth, btnContinueHeight;
    private float btnExitWidth, btnExitHeight;

    private float screenWidth, screenHeight;

    private float buttonExitX, buttonExitY;


    public Pause(LaserDoorsGame game, GameScreen gameScreen) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.gameScreen = gameScreen;

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        this.btnContinueWidth = buttonContinue.getWidth();
        this.btnContinueHeight = buttonContinue.getHeight();
        this.btnExitWidth = buttonExit.getWidth();
        this.btnExitHeight = buttonExit.getHeight();
        this.buttonMenuWidth = buttonMenu.getWidth();
        this.buttonMenuHeight = buttonMenu.getHeight();

        this.buttonContinueX = screenWidth / 2 - btnContinueWidth / 2;
        this.buttonContinueY = screenHeight / 2 - btnContinueHeight / 2 + 50;

        this.buttonExitX = screenWidth / 2 - btnExitWidth / 2;
        this.buttonExitY = screenHeight / 2 - btnExitHeight / 2 - 150;

        this.buttonMenuX = screenWidth / 2 - buttonMenuWidth / 2;
        this.buttonMenuY = screenHeight / 2 - buttonMenuHeight / 2 - 50;
    }


    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.batch.begin();
        this.continueGame();
        this.exitGame();
        this.goToMenu();
        this.batch.end();
    }

    public void goToMenu() {
        if (this.isButtonMenu()
                && screenHeight - Gdx.input.getY() < buttonMenuY + buttonMenuHeight
                && screenHeight - Gdx.input.getY() > buttonMenuY) {
            this.batch.draw(buttonMenuDown, buttonMenuX, buttonMenuY);
            if (Gdx.input.isTouched()) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.game.setScreen(new MainMenu(this.game));
            }
        }
        else {
            this.batch.draw(buttonMenu, buttonMenuX, buttonMenuY);
        }
    }

    public boolean isButtonMenu() {
        return Gdx.input.getX() < buttonMenuX + buttonMenuWidth && Gdx.input.getX() > buttonMenuX;
    }

    public void continueGame() {
        if (this.isButtonContinue()
                && screenHeight - Gdx.input.getY() < buttonContinueY + btnContinueHeight
                && screenHeight - Gdx.input.getY() > buttonContinueY) {
            this.batch.draw(buttonContinueDown, buttonContinueX, buttonContinueY);
            if (Gdx.input.isTouched()) {
                game.setScreen(gameScreen);
            }
        }
        else {
            this.batch.draw(buttonContinue, buttonContinueX, buttonContinueY);
        }
    }

    public boolean isButtonContinue() {
        return Gdx.input.getX() < buttonContinueX + btnContinueWidth && Gdx.input.getX() > buttonContinueX;
    }

    public void exitGame() {
        if (this.isButtonExit()
                && screenHeight - Gdx.input.getY() < buttonExitY + btnExitHeight
                && screenHeight - Gdx.input.getY() > buttonExitY) {
            this.batch.draw(buttonExitDown, buttonExitX, buttonExitY, btnExitWidth, btnExitHeight);
            if (Gdx.input.isTouched()) {
                Gdx.app.exit();
            }
        }
        else {
            this.batch.draw(buttonExit, buttonExitX, buttonExitY, btnExitWidth, btnExitHeight);
        }
    }

    public boolean isButtonExit() {
        return Gdx.input.getX() < buttonExitX + btnExitWidth && Gdx.input.getX() > buttonExitX;
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
        buttonContinue.dispose();
        buttonContinueDown.dispose();
        buttonExit.dispose();
        buttonExitDown.dispose();
        buttonMenu.dispose();
        buttonMenuDown.dispose();
    }
}
