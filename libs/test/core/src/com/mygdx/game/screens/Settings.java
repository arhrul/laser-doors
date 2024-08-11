package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.LaserDoorsGame;
import com.mygdx.game.settings.ControlSettings;
import com.mygdx.game.settings.SoundSettings;

import java.util.Objects;

public class Settings implements Screen {

    private final LaserDoorsGame game;
    private Screen previousScreen;
    private final SpriteBatch batch;

    private final float screenWidth, screenHeight;

    private Stage stage;
    private final Skin skin;

    private final ControlSettings controlSettings;

    private final Table controlTable;
    private final TextField textFieldUp;
    private final TextField textFieldDown;
    private final TextField textFieldLeft;
    private final TextField textFieldRight;
    private final Label labelRight;
    private final Label labelLeft;
    private final Label labelDown;
    private final Label labelUp;

    private final SoundSettings soundSettings;

    private final Table soundTable;
    private final Slider musicSlider;
    private final Slider soundSlider;

    private final Table skinTable;
    private final Image activeImage;
    private final Image standardPlayerImage;
    private final Image orangePlayerImage;
    private final Image mustachePlayerImage;

    /**
     * Settings screen constructor.
     *
     * @param game the main game instance
     */
    public Settings(LaserDoorsGame game) {
        this.game = game;

        this.batch = new SpriteBatch();

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        this.controlSettings = this.game.getControlSettings();
        this.soundSettings = this.game.getSoundSettings();

        this.skin = new Skin(Gdx.files.internal("uiskinNeon/skin/neon-ui.json"));

        this.controlTable = new Table();
        this.textFieldUp = new TextField(controlSettings.getUp(), skin);
        this.textFieldDown = new TextField(controlSettings.getDown(), skin);
        this.textFieldLeft = new TextField(controlSettings.getLeft(), skin);
        this.textFieldRight = new TextField(controlSettings.getRight(), skin);
        this.labelUp = new Label("Up:", skin);
        this.labelDown = new Label("Down:", skin);
        this.labelLeft = new Label("Left:", skin);
        this.labelRight = new Label("Right:", skin);

        this.soundTable = new Table();
        this.musicSlider = new Slider(0, 1, 0.01f, false, skin);
        this.soundSlider = new Slider(0, 1, 0.01f, false, skin);
        this.musicSlider.setValue(this.soundSettings.getBgMusicVolume());
        this.soundSlider.setValue(this.soundSettings.getSoundsVolume());

        this.skinTable = new Table();
        Texture activeTexture = new Texture("frame.png");
        TextureRegionDrawable drawableActive = new TextureRegionDrawable(activeTexture);
        this.activeImage = new Image(drawableActive);

        Texture standardPlayer = new Texture("skins/playerStandard_standard.png");
        TextureRegionDrawable drawableStandard = new TextureRegionDrawable(standardPlayer);
        this.standardPlayerImage = new Image(drawableStandard);

        Texture orangePlayer = new Texture("skins/playerStandard_orange.png");
        TextureRegionDrawable drawableOrange = new TextureRegionDrawable(orangePlayer);
        this.orangePlayerImage = new Image(drawableOrange);

        Texture mustachePlayer = new Texture("skins/playerStandard_mustache.png");
        TextureRegionDrawable drawableMustache = new TextureRegionDrawable(mustachePlayer);
        this.mustachePlayerImage = new Image(drawableMustache);
    }

    @Override
    public void show() {
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this.stage);

        this.drawSettingsButtons();

        this.drawControlSettings();

        this.stage.act(Gdx.graphics.getDeltaTime());
    }

    /**
     * Draw buttons in settings.
     */
    public void drawSettingsButtons() {
        TextButton controlSettingsButton = new TextButton("Control", this.skin);
        controlSettingsButton.setSize(100, 50);
        controlSettingsButton.setPosition(this.screenWidth / 2 - controlSettingsButton.getWidth() / 2 - 500,
                this.screenHeight / 2 - controlSettingsButton.getHeight() / 2 + 400);
        this.stage.addActor(controlSettingsButton);

        TextButton soundSettingsButton = new TextButton("Sound", skin);
        soundSettingsButton.setSize(100, 50);
        soundSettingsButton.setPosition(this.screenWidth / 2 - controlSettingsButton.getWidth() / 2 - 300,
                this.screenHeight / 2 - controlSettingsButton.getHeight() / 2 + 400);
        this.stage.addActor(soundSettingsButton);

        TextButton skinSettingsButton = new TextButton("Skin", skin);
        skinSettingsButton.setSize(100, 50);
        skinSettingsButton.setPosition(this.screenWidth / 2 - controlSettingsButton.getWidth() / 2 - 100,
                this.screenHeight / 2 - controlSettingsButton.getHeight() / 2 + 400);
        this.stage.addActor(skinSettingsButton);

        controlSettingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundSettings.playClickSound();
                controlTable.clear();
                soundTable.clear();
                skinTable.clear();
                activeImage.remove();
                drawControlSettings();
            }
        });

        soundSettingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundSettings.playClickSound();
                controlTable.clear();
                soundTable.clear();
                skinTable.clear();
                activeImage.remove();
                drawSoundSettings();
            }
        });

        skinSettingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundSettings.playClickSound();
                controlTable.clear();
                soundTable.clear();
                skinTable.clear();
                drawSkinSettings();
            }
        });
    }

    /**
     * Draw active player skin frame.
     */
    public void drawActiveFrame() {
        if (Objects.equals(this.game.getSkinSettings().getCurrentSkinType(), "standard")) {
            float xPos = this.standardPlayerImage.getX();
            float yPos = this.standardPlayerImage.getY();
            this.activeImage.setPosition(xPos, yPos);
        } else if (Objects.equals(this.game.getSkinSettings().getCurrentSkinType(), "orange")) {
            float xPos = this.orangePlayerImage.getX();
            float yPos = this.orangePlayerImage.getY();
            activeImage.setPosition(xPos, yPos);
        } else if (Objects.equals(this.game.getSkinSettings().getCurrentSkinType(), "mustache")) {
            float xPos = this.mustachePlayerImage.getX();
            float yPos = this.mustachePlayerImage.getY();
            this.activeImage.setPosition(xPos, yPos);
        }
        if (!this.stage.getActors().contains(this.activeImage, true)) {
            this.stage.addActor(this.activeImage);
        }
    }

    /**
     * Draw skin settings.
     */
    public void drawSkinSettings() {
        this.skinTable.setFillParent(true);
        this.standardPlayerImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                float xPos = standardPlayerImage.getX();
                float yPos = standardPlayerImage.getY();
                activeImage.setPosition(xPos, yPos);
                stage.addActor(activeImage);
                game.getSkinSettings().setCurrentSkinType("standard");
                return true;
            }
        });

        this.orangePlayerImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                float xPos = orangePlayerImage.getX();
                float yPos = orangePlayerImage.getY();
                activeImage.setPosition(xPos, yPos);
                stage.addActor(activeImage);
                game.getSkinSettings().setCurrentSkinType("orange");
                return true;
            }
        });

        this.mustachePlayerImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                float xPos = mustachePlayerImage.getX();
                float yPos = mustachePlayerImage.getY();
                activeImage.setPosition(xPos, yPos);
                stage.addActor(activeImage);
                game.getSkinSettings().setCurrentSkinType("mustache");
                return true;
            }
        });

        this.skinTable.add(this.standardPlayerImage).pad(10);
        this.skinTable.add(this.orangePlayerImage).pad(10);
        this.skinTable.add(this.mustachePlayerImage).pad(10);
        this.stage.addActor(this.skinTable);
        this.stage.draw();
        this.drawActiveFrame();
    }

    /**
     * Update sound volume.
     */
    public void checkSoundVolume() {
        this.soundSettings.setBgMusicVolume(this.musicSlider.getValue());
        this.soundSettings.setSoundsVolume(this.soundSlider.getValue());
    }

    /**
     * Draw sound settings.
     */
    public void drawSoundSettings() {
        this.soundTable.setFillParent(true);
        this.soundTable.add(new Label("Music: ", skin));
        this.soundTable.add(musicSlider).pad(10);
        this.soundTable.row();

        this.soundTable.add(new Label("Sounds: ", skin));
        this.soundTable.add(this.soundSlider).pad(10);
        this.stage.addActor(this.soundTable);
    }

    /**
     * Draw control settings.
     */
    public void drawControlSettings() {
        this.controlTable.setFillParent(true);
        this.stage.addActor(this.controlTable);

        this.textFieldUp.setMaxLength(1);
        this.textFieldUp.setDisabled(false);


        this.textFieldDown.setMaxLength(1);
        this.textFieldDown.setDisabled(false);
        this.changeDownControl(this.textFieldDown);


        this.textFieldLeft.setMaxLength(1);
        this.textFieldLeft.setDisabled(false);
        this.changeLeftControl(this.textFieldLeft);


        this.textFieldRight.setMaxLength(1);
        this.textFieldRight.setDisabled(false);
        this.changeRightControl(this.textFieldRight);


        this.controlTable.add(this.labelUp).pad(10);
        this.controlTable.add(this.textFieldUp).width(30).pad(10);
        this.controlTable.row();

        this.controlTable.add(this.labelDown).pad(10);
        this.controlTable.add(this.textFieldDown).width(30).pad(10);
        this.controlTable.row();

        this.controlTable.add(this.labelLeft).pad(10);
        this.controlTable.add(this.textFieldLeft).width(30).pad(10);
        this.controlTable.row();

        this.controlTable.add(this.labelRight).pad(10);
        this.controlTable.add(this.textFieldRight).width(30).pad(10);
        this.controlTable.row();

        this.controlTable.center();
    }

    /**
     * Change up control key.
     *
     * @param textFieldUp key
     */
    public void changeUpControl(TextField textFieldUp) {
        String key = textFieldUp.getText().toLowerCase();
        this.controlSettings.setUp(key);
    }

    /**
     * Change down control key.
     *
     * @param textFieldDown key
     */
    public void changeDownControl(TextField textFieldDown) {
        String key = textFieldDown.getText().toLowerCase();
        this.controlSettings.setDown(key);
    }

    /**
     * Change left control key.
     *
     * @param textFieldLeft key
     */
    public void changeLeftControl(TextField textFieldLeft) {
        String key = textFieldLeft.getText().toLowerCase();
        this.controlSettings.setLeft(key);
    }

    /**
     * Change right control key.
     *
     * @param textFieldRight key
     */
    public void changeRightControl(TextField textFieldRight) {
        String key = textFieldRight.getText().toLowerCase();
        this.controlSettings.setRight(key);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.batch.begin();

        this.batch.end();
        this.stage.act(delta);
        this.stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.game.setScreen(previousScreen);
        }

        this.checkSoundVolume();


        this.changeUpControl(this.textFieldUp);
        this.changeDownControl(this.textFieldDown);
        this.changeLeftControl(this.textFieldLeft);
        this.changeRightControl(this.textFieldRight);

    }

    /**
     * Set screen, which was previous.
     *
     * @param screen screen
     */
    public void setPreviousScreen(Screen screen) {
        this.previousScreen = screen;
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
        this.batch.dispose();
        this.skin.dispose();
        this.stage.dispose();
    }
}
