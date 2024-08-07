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
import com.mygdx.game.enums.Setting;
import com.mygdx.game.settings.ControlSettings;
import com.mygdx.game.settings.SoundSettings;

import java.util.Objects;

public class Settings implements Screen {

    private LaserDoorsGame game;
    private Screen previousScreen;
    private SpriteBatch batch;

    private float screenWidth, screenHeight;

    private Setting currentSetting;

    private Stage stage;
    private Skin skin;

    private ControlSettings controlSettings;

    private Table controlTable;
    private TextField textFieldUp;
    private TextField textFieldDown;
    private TextField textFieldLeft;
    private TextField textFieldRight;
    private Label labelRight;
    private Label labelLeft;
    private Label labelDown;
    private Label labelUp;

    private SoundSettings soundSettings;

    private Table soundTable;
    private Slider musicSlider;
    private Slider soundSlider;

    private Table skinTable;
    private Image activeImage;
    private Image standardPlayerImage;
    private Image orangePlayerImage;
    private Image mustachePlayerImage;

    /**
     * Settings screen constructor.
     *
     * @param game      the main game instance
     */
    public Settings(LaserDoorsGame game) {
        this.game = game;

        this.batch = new SpriteBatch();

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        this.currentSetting = Setting.CONTROL;
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

    /**
     * Called when this screen becomes the current screen for a {@link LaserDoorsGame}.
     */
    @Override
    public void show() {
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.drawSettingsButtons();

        if (this.currentSetting == Setting.CONTROL) {
            this.drawControlSettings();
        }

        stage.act(Gdx.graphics.getDeltaTime());
    }

    public void drawSettingsButtons() {
        TextButton controlSettingsButton = new TextButton("Control", skin);
        controlSettingsButton.setSize(100, 50);
        controlSettingsButton.setPosition(screenWidth / 2 - controlSettingsButton.getWidth() / 2 - 500,
                screenHeight / 2 - controlSettingsButton.getHeight() / 2 + 400);
        stage.addActor(controlSettingsButton);

        TextButton soundSettingsButton = new TextButton("Sound", skin);
        soundSettingsButton.setSize(100, 50);
        soundSettingsButton.setPosition(screenWidth / 2 - controlSettingsButton.getWidth() / 2 - 300,
                screenHeight / 2 - controlSettingsButton.getHeight() / 2 + 400);
        stage.addActor(soundSettingsButton);

        TextButton skinSettingsButton = new TextButton("Skin", skin);
        skinSettingsButton.setSize(100, 50);
        skinSettingsButton.setPosition(screenWidth / 2 - controlSettingsButton.getWidth() / 2 - 100,
                screenHeight / 2 - controlSettingsButton.getHeight() / 2 + 400);
        stage.addActor(skinSettingsButton);

        controlSettingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundSettings.playClickSound();
                currentSetting = Setting.CONTROL;
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
                currentSetting = Setting.SOUND;
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

    public void drawActiveFrame() {
        if (Objects.equals(this.game.getSkinSettings().getCurrentSkinType(), "standard")) {
            float xPos = standardPlayerImage.getX();
            float yPos = standardPlayerImage.getY();
            activeImage.setPosition(xPos, yPos);
        } else if (Objects.equals(this.game.getSkinSettings().getCurrentSkinType(), "orange")) {
            float xPos = orangePlayerImage.getX();
            float yPos = orangePlayerImage.getY();
            activeImage.setPosition(xPos, yPos);
        } else if (Objects.equals(this.game.getSkinSettings().getCurrentSkinType(), "mustache")) {
            float xPos = mustachePlayerImage.getX();
            float yPos = mustachePlayerImage.getY();
            activeImage.setPosition(xPos, yPos);
        }
        if (!stage.getActors().contains(activeImage, true)) {
            stage.addActor(activeImage);
        }
    }

    public void drawSkinSettings() {
        this.skinTable.setFillParent(true);
        standardPlayerImage.addListener(new InputListener() {
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

        orangePlayerImage.addListener(new InputListener() {
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

        mustachePlayerImage.addListener(new InputListener() {
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

        this.skinTable.add(standardPlayerImage).pad(10);
        this.skinTable.add(orangePlayerImage).pad(10);
        this.skinTable.add(mustachePlayerImage).pad(10);
        this.stage.addActor(skinTable);
        this.stage.draw();
        drawActiveFrame();
    }

    public void checkSoundVolume() {
        this.soundSettings.setBgMusicVolume(this.musicSlider.getValue());
        this.soundSettings.setSoundsVolume(this.soundSlider.getValue());
    }

    public void drawSoundSettings() {
        soundTable.setFillParent(true);
        soundTable.add(new Label("Music: ", skin));
        soundTable.add(musicSlider).pad(10);
        soundTable.row();

        soundTable.add(new Label("Sounds: ", skin));
        soundTable.add(soundSlider).pad(10);
        stage.addActor(soundTable);
    }

    public void drawControlSettings() {
        controlTable.setFillParent(true);
        stage.addActor(controlTable);


        textFieldUp.setMaxLength(1);
        textFieldUp.setDisabled(false);


        textFieldDown.setMaxLength(1);
        textFieldDown.setDisabled(false);
        this.changeDownControl(textFieldDown);


        textFieldLeft.setMaxLength(1);
        textFieldLeft.setDisabled(false);
        this.changeLeftControl(textFieldLeft);


        textFieldRight.setMaxLength(1);
        textFieldRight.setDisabled(false);
        this.changeRightControl(textFieldRight);


        controlTable.add(labelUp).pad(10);
        controlTable.add(textFieldUp).width(30).pad(10);
        controlTable.row();

        controlTable.add(labelDown).pad(10);
        controlTable.add(textFieldDown).width(30).pad(10);
        controlTable.row();

        controlTable.add(labelLeft).pad(10);
        controlTable.add(textFieldLeft).width(30).pad(10);
        controlTable.row();

        controlTable.add(labelRight).pad(10);
        controlTable.add(textFieldRight).width(30).pad(10);
        controlTable.row();

        controlTable.center();
    }

    public void changeUpControl(TextField textFieldUp) {
        String key = textFieldUp.getText().toLowerCase();
        this.controlSettings.setUp(key);
    }

    public void changeDownControl(TextField textFieldDown) {
        String key = textFieldDown.getText().toLowerCase();
        this.controlSettings.setDown(key);
    }

    public void changeLeftControl(TextField textFieldLeft) {
        String key = textFieldLeft.getText().toLowerCase();
        this.controlSettings.setLeft(key);
    }

    public void changeRightControl(TextField textFieldRight) {
        String key = textFieldRight.getText().toLowerCase();
        this.controlSettings.setRight(key);
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
        batch.dispose();
    }
}
