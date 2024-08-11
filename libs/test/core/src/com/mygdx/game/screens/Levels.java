package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.LaserDoorsGame;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Levels implements Screen {

    private static final int STARS_MARGIN = 25;
    private final LaserDoorsGame game;

    private final SpriteBatch batch;

    private final float screenWidth, screenHeight;

    private final Texture buttonOne = new Texture(Gdx.files.internal("buttons/buttonOne.png"));
    private final Texture buttonTwo = new Texture(Gdx.files.internal("buttons/buttonTwo.png"));
    private final Texture buttonThree = new Texture(Gdx.files.internal("buttons/buttonThree.png"));
    private final Texture buttonFour = new Texture(Gdx.files.internal("buttons/buttonFour.png"));
    private final Texture buttonFive = new Texture(Gdx.files.internal("buttons/buttonFive.png"));

    private final Texture buttonOneDown = new Texture("buttons/buttonOneDown.png");
    private final Texture buttonTwoDown = new Texture("buttons/buttonTwoDown.png");
    private final Texture buttonThreeDown = new Texture("buttons/buttonThreeDown.png");
    private final Texture buttonFourDown = new Texture("buttons/buttonFourDown.png");
    private final Texture buttonFiveDown = new Texture("buttons/buttonFiveDown.png");

    private final Texture buttonReset = new Texture("buttons/buttonReset.png");
    private final Texture buttonResetDown = new Texture("buttons/buttonResetDown.png");

    private final Texture buttonMenu = new Texture("buttons/buttonMenu.png");
    private final Texture buttonMenuDown = new Texture("buttons/buttonMenuDown.png");

    private final Texture buttonSettings = new Texture("buttons/buttonSettings.png");
    private final Texture buttonSettingsDown = new Texture("buttons/buttonSettingsDown.png");

    private final float buttonMenuX, buttonMenuY;
    private final float buttonMenuWidth, buttonMenuHeight;

    private final float buttonResetWidth, buttonResetHeight;
    private final float buttonResetX, buttonResetY;

    private final float btnOneWidth, btnOneHeight;
    private final float btnTwoWidth, btnTwoHeight;
    private final float btnThreeWidth, btnThreeHeight;
    private final float btnFourWidth, btnFourHeight;
    private final float btnFiveWidth, btnFiveHeight;

    private final float buttonOneX, buttonOneY;
    private final float buttonTwoX, buttonTwoY;
    private final float buttonThreeX, buttonThreeY;
    private final float buttonFourX, buttonFourY;
    private final float buttonFiveX, buttonFiveY;

    private final float buttonSettingsX, buttonSettingsY;
    private final float btnSettingsWidth, btnSettingsHeight;

    private static final String zeroStars = "stars/zeroStars.png";
    private static final String oneStars = "stars/oneStar.png";
    private static final String twoStars = "stars/twoStars.png";
    private static final String threeStars = "stars/threeStars.png";


    private final Texture starsTexture = new Texture("stars/zeroStars.png");
    private final float starsX, starsY;
    private final float starsWidth, starsHeight;

    private static final String firstLevelFilename = "times/first-level-times.txt";
    private static final String secondLevelFilename = "times/second-level-times.txt";
    private static final String thirdLevelFilename = "times/third-level-times.txt";
    private static final String fourthLevelFilename = "times/fourth-level-times.txt";
    private static final String fifthLevelFilename = "times/fifth-level-times.txt";

    private final List<String> levels = new ArrayList<>(Arrays.asList(
            firstLevelFilename,
            secondLevelFilename,
            thirdLevelFilename,
            fourthLevelFilename,
            fifthLevelFilename
    ));

    private static final String firstLevelMap = "tiled/map1.tmx";
    private static final String secondLevelMap = "tiled/map2.tmx";
    private static final String thirdLevelMap = "tiled/map3.tmx";
    private static final String fourthLevelMap = "tiled/map4.tmx";
    private static final String fifthLevelMap = "tiled/map5.tmx";

    /**
     * Levels screen.
     *
     * @param game game
     */
    public Levels(LaserDoorsGame game) {
        this.game = game;
        this.batch = new SpriteBatch();

        this.btnOneWidth = buttonOne.getWidth();
        this.btnOneHeight = buttonOne.getHeight();
        this.btnTwoWidth = buttonTwo.getWidth();
        this.btnTwoHeight = buttonTwo.getHeight();
        this.btnThreeWidth = buttonThree.getWidth();
        this.btnThreeHeight = buttonThree.getHeight();
        this.btnFourWidth = buttonFour.getWidth();
        this.btnFourHeight = buttonFour.getHeight();
        this.btnFiveWidth = buttonFive.getWidth();
        this.btnFiveHeight = buttonFive.getHeight();

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        this.buttonOneX = screenWidth / 2 - btnOneWidth / 2;
        this.buttonOneY = screenHeight / 2 - btnOneHeight / 2 + 275;

        this.buttonTwoX = screenWidth / 2 - btnTwoWidth / 2;
        this.buttonTwoY = screenHeight / 2 - btnTwoHeight / 2 + 150;

        this.buttonThreeX = screenWidth / 2 - btnThreeWidth / 2;
        this.buttonThreeY = screenHeight / 2 - btnThreeHeight / 2 + 25;

        this.buttonFourX = screenWidth / 2 - btnFourWidth / 2;
        this.buttonFourY = screenHeight / 2 - btnFourHeight / 2 - 110;

        this.buttonFiveX = screenWidth / 2 - btnFiveWidth / 2;
        this.buttonFiveY = screenHeight / 2 - btnFiveHeight / 2 - 235;

        this.btnSettingsWidth = 60;
        this.btnSettingsHeight = 60;

        this.starsWidth = (float) this.starsTexture.getWidth() / 25;
        this.starsHeight = (float) this.starsTexture.getHeight() / 25;

        this.starsX = screenWidth / 2 - starsWidth / 2;
        this.starsY = screenHeight / 2 - starsHeight / 2;

        this.buttonResetWidth = this.buttonReset.getWidth();
        this.buttonResetHeight = this.buttonReset.getHeight();
        this.buttonResetX = screenWidth - buttonResetWidth - buttonResetWidth / 2;
        this.buttonResetY = buttonResetHeight - buttonResetHeight / 2;

        this.buttonMenuWidth = buttonMenu.getWidth();
        this.buttonMenuHeight = buttonMenu.getHeight();

        this.buttonMenuX = buttonMenuWidth - buttonMenuWidth / 2;
        this.buttonMenuY = buttonMenuHeight - buttonResetHeight / 2;

        this.buttonSettingsX = screenWidth - btnSettingsWidth * 1.5f;
        this.buttonSettingsY = screenHeight - btnSettingsHeight * 1.5f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.batch.begin();
        this.runFirstLevel();
        this.runSecondLevel();
        this.runThirdLevel();
        this.runFourthLevel();
        this.runFifthLevel();
        this.runTestLevel();
        this.drawResetButton();
        this.goToMenu();
        this.drawSettings();
        this.batch.end();
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
     * Draw menu button.
     */
    public void goToMenu() {
        if (this.isButtonMenu()) {
            this.batch.draw(buttonMenuDown, buttonMenuX, buttonMenuY);
            if (Gdx.input.isTouched()) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.game.getSoundSettings().playClickSound();
                this.game.setScreen(new MainMenu(this.game));
            }
        } else {
            this.batch.draw(buttonMenu, buttonMenuX, buttonMenuY);
        }
    }

    /**
     * Check if button menu is hovered.
     *
     * @return boolean
     */
    public boolean isButtonMenu() {
        return Gdx.input.getX() < buttonMenuX + buttonMenuWidth
                && Gdx.input.getX() > buttonMenuX
                && screenHeight - Gdx.input.getY() < buttonMenuY + buttonMenuHeight
                && screenHeight - Gdx.input.getY() > buttonMenuY;
    }

    /**
     * Draw stars near to the levels.
     *
     * @param filename path to texture
     * @param x        x-pos
     * @param y        y-pos
     */
    public void drawStars(String filename, float x, float y) {
        List<Float> levelTimes;
        String starsPath;
        Path levelPath = Paths.get(filename);
        try {
            levelTimes = Files.readAllLines(levelPath).stream().map(Float::valueOf).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        float bestTime = levelTimes.stream().min(Float::compareTo).orElse(0f);
        int bestTimeForLevel = this.findBestTimeForLevel(filename);

        if (levelTimes.isEmpty()) {
            starsPath = zeroStars;
        } else if (bestTime <= bestTimeForLevel) {
            starsPath = threeStars;
        } else if (bestTime > bestTimeForLevel && bestTime <= (bestTimeForLevel + 5)) {
            starsPath = twoStars;
        } else if (bestTime > (bestTimeForLevel + 5) && bestTime <= (bestTimeForLevel + 10)) {
            starsPath = oneStars;
        } else {
            starsPath = zeroStars;
        }

        Texture starsTexture = new Texture(starsPath);

        this.batch.draw(starsTexture, x, y, starsWidth, starsHeight);
    }

    /**
     * Get best time for the level.
     *
     * @param filename level
     * @return time
     */
    public int findBestTimeForLevel(String filename) {
        if (filename.equals(firstLevelFilename)) {
            return 17;
        } else if (filename.equals(secondLevelFilename)) {
            return 22;
        } else if (filename.equals(thirdLevelFilename)) {
            return 27;
        } else if (filename.equals(fourthLevelFilename)) {
            return 32;
        } else {
            return 37;
        }
    }

    /**
     * First level button.
     */
    public void runFirstLevel() {
        this.drawStars(firstLevelFilename, starsX, buttonOneY - STARS_MARGIN);
        if (this.isButtonOne()) {
            this.batch.draw(buttonOneDown, buttonOneX, buttonOneY);
            if (Gdx.input.isTouched()) {
                this.game.getSoundSettings().playClickSound();
                game.setScreen(new GameScreen(game, firstLevelMap));
            }
        } else {
            this.batch.draw(buttonOne, buttonOneX, buttonOneY);
        }
    }

    /**
     * Check mouse X coordinate on first level button.
     *
     * @return true or false
     */
    public boolean isButtonOne() {
        return Gdx.input.getX() < buttonOneX + btnOneWidth
                && Gdx.input.getX() > buttonOneX
                && screenHeight - Gdx.input.getY() < buttonOneY + btnOneHeight
                && screenHeight - Gdx.input.getY() > buttonOneY;
    }

    /**
     * Second level button.
     */
    public void runSecondLevel() {
        this.drawStars(secondLevelFilename, starsX, buttonTwoY - STARS_MARGIN);
        if (this.isButtonTwo()) {
            this.batch.draw(buttonTwoDown, buttonTwoX, buttonTwoY);
            if (Gdx.input.isTouched()) {
                this.game.getSoundSettings().playClickSound();
                game.setScreen(new GameScreen(game, secondLevelMap));
            }
        } else {
            this.batch.draw(buttonTwo, buttonTwoX, buttonTwoY);
        }
    }

    /**
     * Check if second level button is hovered.
     *
     * @return boolean
     */
    public boolean isButtonTwo() {
        return Gdx.input.getX() < buttonTwoX + btnTwoWidth
                && Gdx.input.getX() > buttonTwoX
                && screenHeight - Gdx.input.getY() < buttonTwoY + btnTwoHeight
                && screenHeight - Gdx.input.getY() > buttonTwoY;
    }

    /**
     * Third level button.
     */
    public void runThirdLevel() {
        this.drawStars(thirdLevelFilename, starsX, buttonThreeY - STARS_MARGIN);
        if (this.isButtonThree()) {
            this.batch.draw(buttonThreeDown, buttonThreeX, buttonThreeY);
            if (Gdx.input.isTouched()) {
                this.game.getSoundSettings().playClickSound();
                game.setScreen(new GameScreen(game, thirdLevelMap));
            }
        } else {
            this.batch.draw(buttonThree, buttonThreeX, buttonThreeY);
        }
    }

    /**
     * Check if third level button is hovered.
     *
     * @return boolean
     */
    public boolean isButtonThree() {
        return Gdx.input.getX() < buttonThreeX + btnThreeWidth
                && Gdx.input.getX() > buttonThreeX
                && screenHeight - Gdx.input.getY() < buttonThreeY + btnThreeHeight
                && screenHeight - Gdx.input.getY() > buttonThreeY;
    }

    /**
     * Fourth level button.
     */
    public void runFourthLevel() {
        this.drawStars(fourthLevelFilename, starsX, buttonFourY - STARS_MARGIN);
        if (this.isButtonFour()) {
            this.batch.draw(buttonFourDown, buttonFourX, buttonFourY);
            if (Gdx.input.isTouched()) {
                this.game.getSoundSettings().playClickSound();
                game.setScreen(new GameScreen(game, fourthLevelMap));
            }
        } else {
            this.batch.draw(buttonFour, buttonFourX, buttonFourY);
        }
    }

    /**
     * Check if fourth level button is hovered.
     *
     * @return boolean
     */
    public boolean isButtonFour() {
        return Gdx.input.getX() < buttonFourX + btnFourWidth
                && Gdx.input.getX() > buttonFourX
                && screenHeight - Gdx.input.getY() < buttonFourY + btnFiveHeight
                && screenHeight - Gdx.input.getY() > buttonFourY;
    }

    /**
     * Fifth level button.
     */
    public void runFifthLevel() {
        this.drawStars(fifthLevelFilename, starsX, buttonFiveY - STARS_MARGIN);
        if (this.isButtonFive()) {
            this.batch.draw(buttonFiveDown, buttonFiveX, buttonFiveY);
            if (Gdx.input.isTouched()) {
                this.game.getSoundSettings().playClickSound();
                game.setScreen(new GameScreen(game, fifthLevelMap));
            }
        } else {
            this.batch.draw(buttonFive, buttonFiveX, buttonFiveY);
        }
    }

    /**
     * Check if fifth level button is hovered.
     *
     * @return boolean
     */
    public boolean isButtonFive() {
        return Gdx.input.getX() < buttonFiveX + btnFiveWidth
                && Gdx.input.getX() > buttonFiveX
                && screenHeight - Gdx.input.getY() < buttonFiveY + btnFiveHeight
                && screenHeight - Gdx.input.getY() > buttonFiveY;
    }

    /**
     * Special combination for running test level.
     */
    public void runTestLevel() {
        if (Gdx.input.isKeyPressed(Input.Keys.L) &&
                Gdx.input.isKeyPressed(Input.Keys.A) &&
                Gdx.input.isKeyPressed(Input.Keys.S) &&
                Gdx.input.isKeyPressed(Input.Keys.E) &&
                Gdx.input.isKeyPressed(Input.Keys.R)) {
            game.setScreen(new GameScreen(game, "tiled/mapTest.tmx"));
        }
        ;
    }

    /**
     * Draw reset button.
     */
    public void drawResetButton() {
        if (this.isResetButton()) {
            this.batch.draw(buttonResetDown, buttonResetX, buttonResetY);
            if (Gdx.input.isTouched()) {
                this.resetLevels();
            }
        } else {
            this.batch.draw(buttonReset, buttonResetX, buttonResetY);
        }
    }

    /**
     * Check if reset button is hovered.
     *
     * @return boolean
     */
    public boolean isResetButton() {
        return Gdx.input.getX() < buttonResetX + buttonResetWidth
                && Gdx.input.getX() > buttonResetX
                && screenHeight - Gdx.input.getY() < buttonResetY + buttonResetHeight
                && screenHeight - Gdx.input.getY() > buttonResetY;
    }

    /**
     * Reset level times.
     */
    public void resetLevels() {
        for (String path : levels) {
            try (FileWriter writer = new FileWriter(path)) {
                writer.write("");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
        buttonOne.dispose();
        buttonTwo.dispose();
        buttonThree.dispose();
        buttonFour.dispose();
        buttonFive.dispose();
        buttonOneDown.dispose();
        buttonTwoDown.dispose();
        buttonThreeDown.dispose();
        buttonFourDown.dispose();
        buttonFiveDown.dispose();
        buttonReset.dispose();
        buttonResetDown.dispose();
        buttonMenu.dispose();
        buttonMenuDown.dispose();
        starsTexture.dispose();
    }
}
