package com.mygdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.enums.Directions;
import com.mygdx.game.enums.DoorTypes;
import com.mygdx.game.screens.GameOver;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.YouWin;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Player {
    private Texture textureStandard;
    private Texture textureRed;
    private Texture textureGreen;

    private Texture currentTexture;

    private final GameScreen gameScreen;

    private final float playerWidth;
    private final float playerHeight;

    private final Vector2 position;
    private final Vector2 velocity;

    private final float speed;
    private final Rectangle bounds;

    private static final float acceleration = 500f;
    private static final float deceleration = 500f;

    private boolean onRedDoor = false;
    private boolean onGreenDoor = false;

    private Vector2 lastPosition;

    private boolean prohibitMovingUp = false;
    private boolean prohibitMovingDown = false;
    private boolean prohibitMovingLeft = false;
    private boolean prohibitMovingRight = false;

    private DoorTypes doorType;
    private Directions currentDirection;

    private String keyUp;
    private String keyDown;
    private String keyLeft;
    private String keyRight;


    /**
     * Player constructor.
     *
     * @param gameScreen game screen
     * @param x          x
     * @param y          y
     * @param speed      speed
     */
    public Player(GameScreen gameScreen, float x, float y, float speed) {
        this.playerWidth = 32;
        this.playerHeight = 32;

        this.position = new Vector2(x, y);
        this.lastPosition = new Vector2(x, y);

        this.velocity = new Vector2(0, 0);
        this.speed = speed;
        this.bounds = new Rectangle(x, y, playerWidth, playerHeight);

        this.gameScreen = gameScreen;

        this.textureStandard = this.gameScreen.getGame().getSkinSettings().getStandardCurrentSkin();
        this.textureRed = this.gameScreen.getGame().getSkinSettings().getRedCurrentSkin();
        this.textureGreen = this.gameScreen.getGame().getSkinSettings().getGreenCurrentSkin();
        this.currentTexture = this.textureStandard;
    }

    /**
     * Update player
     *
     * @param delta delta time
     */
    public void update(float delta) {
        this.updateKeys();
        this.lastPosition = new Vector2(this.position.x, this.position.y);
        if (Gdx.input.isKeyPressed(Input.Keys.valueOf(keyLeft.toUpperCase())) && !this.prohibitMovingLeft) {
            this.velocity.x -= acceleration * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.valueOf(keyRight.toUpperCase())) && !this.prohibitMovingRight) {
            this.velocity.x += acceleration * delta;
        } else {
            if (this.velocity.x > 0) {
                this.velocity.x -= deceleration * delta;
                if (this.velocity.x < 0) this.velocity.x = 0;
            } else if (this.velocity.x < 0) {
                this.velocity.x += deceleration * delta;
                if (this.velocity.x > 0) this.velocity.x = 0;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyUp.toUpperCase())) && !this.prohibitMovingUp) {
            this.velocity.y += acceleration * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyDown.toUpperCase())) && !this.prohibitMovingDown) {
            this.velocity.y -= acceleration * delta;
        } else {
            if (this.velocity.y > 0) {
                this.velocity.y -= deceleration * delta;
                if (this.velocity.y < 0) velocity.y = 0;
            } else if (this.velocity.y < 0) {
                this.velocity.y += deceleration * delta;
                if (this.velocity.y > 0) velocity.y = 0;
            }
        }

        if (this.velocity.len() > speed) {
            this.velocity.nor().scl(speed);
        }

        float newX = this.position.x + this.velocity.x * delta;
        float newY = this.position.y + this.velocity.y * delta;

        this.bounds.setPosition(newX, newY);
        for (Rectangle rectangle : this.gameScreen.getCollisionRectangles()) {
            if (this.bounds.overlaps(rectangle)) {
                newX = this.position.x;
                newY = this.position.y;
                this.velocity.set(0, 0);
            }
        }

        this.position.set(newX, newY);
        this.bounds.setPosition(newX, newY);
        this.gameScreen.updatePlayerPosition(newX, newY);

        if (!this.onRedDoor && !this.onGreenDoor) {
            this.setCurrentDirection(this.position, this.lastPosition);
        }
        checkDoors();
        checkRedFinish();
        checkGreenFinish();
        checkStandardFinish();
        checkHorizontalDoors();
        checkVerticalDoors();
        updateTexture();
    }

    public void updateTexture() {
        this.textureStandard = this.gameScreen.getGame().getSkinSettings().getStandardCurrentSkin();
        this.textureRed = this.gameScreen.getGame().getSkinSettings().getRedCurrentSkin();
        this.textureGreen = this.gameScreen.getGame().getSkinSettings().getGreenCurrentSkin();
        this.currentTexture = this.gameScreen.getGame().getSkinSettings().getCurrentSkinTexture();
    }

    public void updateKeys() {
        this.keyUp = this.gameScreen.getGame().getControlSettings().getUp();
        this.keyDown = this.gameScreen.getGame().getControlSettings().getDown();
        this.keyLeft = this.gameScreen.getGame().getControlSettings().getLeft();
        this.keyRight = this.gameScreen.getGame().getControlSettings().getRight();
    }

    /**
     * Save time.
     */
    public void saveTime() {
        String path;
        if (Objects.equals(this.gameScreen.getMapPath(), "tiled/map1.tmx")) {
            path = "times/first-level-times.txt";
        } else if (Objects.equals(this.gameScreen.getMapPath(), "tiled/map2.tmx")) {
            path = "times/second-level-times.txt";
        } else if (Objects.equals(this.gameScreen.getMapPath(), "tiled/map3.tmx")) {
            path = "times/third-level-times.txt";
        } else if (Objects.equals(this.gameScreen.getMapPath(), "tiled/map4.tmx")) {
            path = "times/fourth-level-times.txt";
        } else {
            path = "times/fifth-level-times.txt";
        }
        try (FileWriter writer = new FileWriter(path, true)) {
            writer.write(Float.toString(this.gameScreen.getTimePassed()));
            writer.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check stars.
     *
     * @return path to stars
     */
    public String checkStars() {
        String path;
        int bestTimeForLevel = this.findBestTimeForLevel();
        if (this.gameScreen.getTimePassed() <= bestTimeForLevel) {
            path = "stars/threeStars.png";
        } else if (this.gameScreen.getTimePassed() > bestTimeForLevel
                && this.gameScreen.getTimePassed() <= (bestTimeForLevel + 5)) {
            path = "stars/twoStars.png";
        } else if (this.gameScreen.getTimePassed() > (bestTimeForLevel + 5)
                && this.gameScreen.getTimePassed() <= (bestTimeForLevel + 10)) {
            path = "stars/oneStar.png";
        } else {
            path = "stars/zeroStars.png";
        }
        return path;
    }

    /**
     * Get best time for level.
     *
     * @return best time
     */
    public int findBestTimeForLevel() {
        int filename = this.gameScreen.getCurrentLevel();
        if (filename == 0) {
            return 17;
        } else if (filename == 1) {
            return 22;
        } else if (filename == 2) {
            return 27;
        } else if (filename == 3) {
            return 32;
        } else {
            return 37;
        }
    }

    /**
     * Check standard finish.
     */
    public void checkStandardFinish() {
        for (Rectangle rectangle : this.gameScreen.getStandardFinishRectangles()) {
            if (rectangle.overlaps(this.bounds)) {
                this.saveTime();
                String path = checkStars();
                if (this.gameScreen.getCurrentLevel() == 4) {
                    this.fifthLevelFinish(path);
                } else {
                    this.gameScreen.getGame().setScreen(
                            new YouWin(this.gameScreen.getGame(),
                                    path,
                                    GameScreen.getLevels().get(this.gameScreen.getCurrentLevel() + 1))
                    );
                }
            }
        }
    }

    /**
     * Check red finish.
     */
    public void checkRedFinish() {
        for (Rectangle rectangle : this.gameScreen.getRedFinishRectangles()) {
            if (rectangle.overlaps(this.bounds)) {
                if (this.currentTexture == this.textureRed) {
                    this.gameScreen.getGame().setScreen(new GameOver(this.gameScreen.getGame(),
                            GameScreen.getLevels().get(this.gameScreen.getCurrentLevel())));
                } else {
                    this.saveTime();
                    String path = checkStars();
                    if (this.gameScreen.getCurrentLevel() == 4) {
                        this.fifthLevelFinish(path);
                    } else {
                        this.gameScreen.getGame().setScreen(
                                new YouWin(this.gameScreen.getGame(),
                                        path,
                                        GameScreen.getLevels().get(this.gameScreen.getCurrentLevel() + 1))
                        );
                    }
                }
            }
        }
    }

    /**
     * Check green finish.
     */
    public void checkGreenFinish() {
        for (Rectangle rectangle : this.gameScreen.getGreenFinishRectangles()) {
            if (rectangle.overlaps(this.bounds)) {
                if (this.currentTexture == textureGreen) {
                    this.gameScreen.getGame().setScreen(new GameOver(this.gameScreen.getGame(),
                            GameScreen.getLevels().get(this.gameScreen.getCurrentLevel() + 1)));
                } else {
                    this.saveTime();
                    String path = checkStars();
                    if (this.gameScreen.getCurrentLevel() == 4) {
                        this.fifthLevelFinish(path);
                    } else {
                        this.gameScreen.getGame().setScreen(
                                new YouWin(this.gameScreen.getGame(),
                                        path,
                                        GameScreen.getLevels().get(this.gameScreen.getCurrentLevel() + 1))
                        );
                    }
                }
            }
        }
    }

    /**
     * Finish the fifth level.
     *
     * @param path stars path
     */
    public void fifthLevelFinish(String path) {
        this.gameScreen.getGame().setScreen(
                new YouWin(this.gameScreen.getGame(),
                        path, "LastLevel")
        );
    }

    /**
     * Check doors.
     */
    public void checkDoors() {
        this.allowMovement();
        boolean wasOnRedDoor = this.onRedDoor;
        boolean wasOnGreenDoor = this.onGreenDoor;
        this.onRedDoor = false;
        this.onGreenDoor = false;


        for (Rectangle rectangle : this.gameScreen.getRedDoorsRectangles()) {
            if (this.bounds.overlaps(rectangle)) {
                this.onRedDoor = true;
                if (!Objects.equals(this.currentTexture.toString(), this.textureRed.toString())) {
                    this.gameScreen.getGame().getSkinSettings().setCurrentColor("Red");
                    this.currentTexture = this.gameScreen.getGame().getSkinSettings().getRedCurrentSkin();
                    this.gameScreen.getGame().getSoundSettings().playDoorSound();
                } else {
                    if (!wasOnRedDoor) {
                        this.gameScreen.getGame().setScreen(new GameOver(this.gameScreen.getGame(),
                                GameScreen.getLevels().get(this.gameScreen.getCurrentLevel() + 1)));
                    }
                }
                prohibitMovementBack();
                break;
            }
        }

        for (Rectangle rectangle : this.gameScreen.getGreenDoorsRectangles()) {
            if (bounds.overlaps(rectangle)) {
                this.onGreenDoor = true;
                if (!Objects.equals(currentTexture.toString(), textureGreen.toString())) {
                    this.gameScreen.getGame().getSkinSettings().setCurrentColor("Green");
                    this.currentTexture = this.gameScreen.getGame().getSkinSettings().getGreenCurrentSkin();
                    this.gameScreen.getGame().getSoundSettings().playDoorSound();

                } else {
                    if (!wasOnGreenDoor) {
                        this.gameScreen.getGame().setScreen(new GameOver(this.gameScreen.getGame(),
                                GameScreen.getLevels().get(this.gameScreen.getCurrentLevel() + 1)));
                    }
                }
                this.prohibitMovementBack();
                break;
            }
        }
    }

    /**
     * Check horizontal doors.
     */
    public void checkHorizontalDoors() {
        for (Rectangle rectangle : this.gameScreen.getHorizontalDoorsRectangles()) {
            if (this.bounds.overlaps(rectangle)) {
                this.doorType = DoorTypes.HORIZONTAL;
            }
        }
    }

    /**
     * Check vertical doors.
     */
    public void checkVerticalDoors() {
        for (Rectangle rectangle : this.gameScreen.getVerticalDoorsRectangles()) {
            if (this.bounds.overlaps(rectangle)) {
                this.doorType = DoorTypes.VERTICAL;
            }
        }
    }

    /**
     * Prohibit movement back if player touches the door.
     */
    public void prohibitMovementBack() {
        if (this.doorType == DoorTypes.HORIZONTAL && (this.currentDirection == Directions.UP
                || this.currentDirection == Directions.UP_LEFT
                || this.currentDirection == Directions.UP_RIGHT)) {
            this.prohibitMovingDown = true;
        } else if (this.doorType == DoorTypes.HORIZONTAL && (this.currentDirection == Directions.DOWN
                || this.currentDirection == Directions.DOWN_LEFT
                || this.currentDirection == Directions.DOWN_RIGHT)) {
            this.prohibitMovingUp = true;
        } else if (this.doorType == DoorTypes.VERTICAL && (this.currentDirection == Directions.LEFT
                || this.currentDirection == Directions.DOWN_LEFT
                || this.currentDirection == Directions.UP_LEFT)) {
            this.prohibitMovingRight = true;

        } else if (this.doorType == DoorTypes.VERTICAL && (this.currentDirection == Directions.RIGHT
                || this.currentDirection == Directions.DOWN_RIGHT
                || this.currentDirection == Directions.UP_RIGHT
        )) {
            this.prohibitMovingLeft = true;
        }
    }

    /**
     * Allow movement after crossing the door.
     */
    public void allowMovement() {
        this.prohibitMovingUp = false;
        this.prohibitMovingDown = false;
        this.prohibitMovingLeft = false;
        this.prohibitMovingRight = false;
    }

    /**
     * Set current direction.
     *
     * @param currentPos current position
     * @param lastPos    last position
     */
    public void setCurrentDirection(Vector2 currentPos, Vector2 lastPos) {
        if (currentPos.x - lastPos.x > 0
                && currentPos.y - lastPos.y == 0
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyRight.toUpperCase()))) {
            this.currentDirection = Directions.RIGHT;
        } else if (currentPos.x - lastPos.x < 0
                && currentPos.y - lastPos.y == 0
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyLeft.toUpperCase()))) {
            this.currentDirection = Directions.LEFT;
        } else if (currentPos.x - lastPos.x == 0
                && currentPos.y - lastPos.y > 0
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyUp.toUpperCase()))) {
            this.currentDirection = Directions.UP;
        } else if (currentPos.x - lastPos.x == 0
                && currentPos.y - lastPos.y < 0
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyDown.toUpperCase()))) {
            this.currentDirection = Directions.DOWN;
        } else if (currentPos.x - lastPos.x > 0
                && currentPos.y - lastPos.y > 0
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyUp.toUpperCase()))
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyRight.toUpperCase()))) {
            this.currentDirection = Directions.UP_RIGHT;
        } else if (currentPos.x - lastPos.x < 0
                && currentPos.y - lastPos.y < 0
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyDown.toUpperCase()))
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyLeft.toUpperCase()))) {
            this.currentDirection = Directions.DOWN_LEFT;
        } else if (currentPos.x - lastPos.x > 0
                && currentPos.y - lastPos.y < 0
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyDown.toUpperCase()))
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyRight.toUpperCase()))) {
            this.currentDirection = Directions.DOWN_RIGHT;
        } else if (currentPos.x - lastPos.x < 0
                && currentPos.y - lastPos.y > 0
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyUp.toUpperCase()))
                && Gdx.input.isKeyPressed(Input.Keys.valueOf(this.keyLeft.toUpperCase()))) {
            this.currentDirection = Directions.UP_LEFT;
        }
    }

    /**
     * Render player.
     *
     * @param batch sprite batch
     */
    public void render(SpriteBatch batch) {
        batch.draw(this.currentTexture, this.position.x, this.position.y, this.playerWidth, this.playerHeight);
    }

    /**
     * Dispose.
     */
    public void dispose() {
        this.textureRed.dispose();
        this.textureGreen.dispose();
    }
}
