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
    private Texture textureRed = new Texture("playerRed.png");
    private Texture textureGreen = new Texture("playerGreen.png");

    private Texture currentTexture;

    private GameScreen gameScreen;

    private float playerWidth, playerHeight;

    private Vector2 position;
    private Vector2 velocity;

    private float speed;
    private Rectangle bounds;

    private float acceleration = 500f;
    private float deceleration = 500f;

    private boolean onRedDoor = false;
    private boolean onGreenDoor = false;

    private Vector2 lastPosition;

    private boolean prohibitMovingUp = false;
    private boolean prohibitMovingDown = false;
    private boolean prohibitMovingLeft = false;
    private boolean prohibitMovingRight = false;

    private DoorTypes doorType;
    private Directions currentDirection;


    /**
     * Player constructor.
     *
     * @param gameScreen game screen
     * @param x          x
     * @param y          y
     * @param speed      speed
     */
    public Player(GameScreen gameScreen, float x, float y, float speed, Texture currentTexture) {
        this.playerWidth = 32;
        this.playerHeight = 32;
        this.position = new Vector2(x, y);
        this.lastPosition = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.speed = speed;
        this.bounds = new Rectangle(x, y, playerWidth, playerHeight);
        this.gameScreen = gameScreen;
        this.currentTexture = currentTexture;
    }

    /**
     * Update player
     *
     * @param delta delta time
     */
    public void update(float delta) {
        lastPosition = new Vector2(position.x, position.y);
        if (Gdx.input.isKeyPressed(Input.Keys.A) && !prohibitMovingLeft) {
            velocity.x -= acceleration * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && !prohibitMovingRight) {
            velocity.x += acceleration * delta;
        } else {
            if (velocity.x > 0) {
                velocity.x -= deceleration * delta;
                if (velocity.x < 0) velocity.x = 0;
            } else if (velocity.x < 0) {
                velocity.x += deceleration * delta;
                if (velocity.x > 0) velocity.x = 0;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && !prohibitMovingUp) {
            velocity.y += acceleration * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) && !prohibitMovingDown) {
            velocity.y -= acceleration * delta;
        } else {
            if (velocity.y > 0) {
                velocity.y -= deceleration * delta;
                if (velocity.y < 0) velocity.y = 0;
            } else if (velocity.y < 0) {
                velocity.y += deceleration * delta;
                if (velocity.y > 0) velocity.y = 0;
            }
        }

        if (velocity.len() > speed) {
            velocity.nor().scl(speed);
        }

        float newX = position.x + velocity.x * delta;
        float newY = position.y + velocity.y * delta;

        bounds.setPosition(newX, newY);
        for (Rectangle rectangle : this.gameScreen.getCollisionRectangles()) {
            if (bounds.overlaps(rectangle)) {
                newX = position.x;
                newY = position.y;
                velocity.set(0, 0);
            }
        }

        position.set(newX, newY);
        bounds.setPosition(newX, newY);
        this.gameScreen.updatePlayerPosition(newX, newY);

        if (!this.onRedDoor && !this.onGreenDoor) {
            this.setCurrentDirection(position, lastPosition);
        }
        checkDoors();
        checkRedFinish();
        checkGreenFinish();
        checkStandardFinish();
        checkHorizontalDoors();
        checkVerticalDoors();
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
        if (gameScreen.getTimePassed() <= bestTimeForLevel) {
            path = "stars/threeStars.png";
        } else if (gameScreen.getTimePassed() > bestTimeForLevel
                && gameScreen.getTimePassed() <= (bestTimeForLevel + 5)) {
            path = "stars/twoStars.png";
        } else if (gameScreen.getTimePassed() > (bestTimeForLevel + 5)
                && gameScreen.getTimePassed() <= (bestTimeForLevel + 10)) {
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
     * Check finish.
     *
     * @param rectangle type of finish
     */
    public void checkFinish(Rectangle rectangle) {

    }

    /**
     * Check standard finish.
     */
    public void checkStandardFinish() {
        for (Rectangle rectangle : this.gameScreen.getStandardFinishRectangles()) {
            if (rectangle.overlaps(bounds)) {
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
            if (rectangle.overlaps(bounds)) {
                if (this.currentTexture == textureRed) {
                    this.gameScreen.getGame().setScreen(new GameOver(this.gameScreen.getGame()));
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
            if (rectangle.overlaps(bounds)) {
                if (this.currentTexture == textureGreen) {
                    this.gameScreen.getGame().setScreen(new GameOver(this.gameScreen.getGame()));
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
        allowMovement();
        boolean wasOnRedDoor = onRedDoor;
        boolean wasOnGreenDoor = onGreenDoor;
        onRedDoor = false;
        onGreenDoor = false;


        for (Rectangle rectangle : this.gameScreen.getRedDoorsRectangles()) {
            if (bounds.overlaps(rectangle)) {
                onRedDoor = true;
                if (!Objects.equals(currentTexture.toString(), textureRed.toString())) {
                    currentTexture = textureRed;
                } else {
                    if (!wasOnRedDoor) {
                        gameScreen.getGame().setScreen(new GameOver(gameScreen.getGame()));
                    }
                }
                prohibitMovementBack();
                break;
            }
        }

        for (Rectangle rectangle : this.gameScreen.getGreenDoorsRectangles()) {
            if (bounds.overlaps(rectangle)) {
                onGreenDoor = true;
                if (!Objects.equals(currentTexture.toString(), textureGreen.toString())) {
                    currentTexture = textureGreen;

                } else {
                    if (!wasOnGreenDoor) {
                        gameScreen.getGame().setScreen(new GameOver(gameScreen.getGame()));
                    }
                }
                prohibitMovementBack();
                break;
            }
        }
    }

    /**
     * Check horizontal doors.
     */
    public void checkHorizontalDoors() {
        for (Rectangle rectangle : this.gameScreen.getHorizontalDoorsRectangles()) {
            if (bounds.overlaps(rectangle)) {
                this.doorType = DoorTypes.HORIZONTAL;
            }
        }
    }

    /**
     * Check vertical doors.
     */
    public void checkVerticalDoors() {
        for (Rectangle rectangle : this.gameScreen.getVerticalDoorsRectangles()) {
            if (bounds.overlaps(rectangle)) {
                this.doorType = DoorTypes.VERTICAL;
            }
        }
    }

    /**
     * Prohibit movement back if player touches the door.
     */
    public void prohibitMovementBack() {
        if (this.doorType == DoorTypes.HORIZONTAL && (currentDirection == Directions.UP
                || currentDirection == Directions.UP_LEFT
                || currentDirection == Directions.UP_RIGHT)) {
            this.prohibitMovingDown = true;
        } else if (this.doorType == DoorTypes.HORIZONTAL && (currentDirection == Directions.DOWN
                || currentDirection == Directions.DOWN_LEFT
                || currentDirection == Directions.DOWN_RIGHT)) {
            this.prohibitMovingUp = true;
        } else if (this.doorType == DoorTypes.VERTICAL && (currentDirection == Directions.LEFT
                || currentDirection == Directions.DOWN_LEFT
                || currentDirection == Directions.UP_LEFT)) {
            this.prohibitMovingRight = true;

        } else if (this.doorType == DoorTypes.VERTICAL && (currentDirection == Directions.RIGHT
                || currentDirection == Directions.DOWN_RIGHT
                || currentDirection == Directions.UP_RIGHT
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
                && Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.currentDirection = Directions.RIGHT;
        } else if (currentPos.x - lastPos.x < 0
                && currentPos.y - lastPos.y == 0
                && Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.currentDirection = Directions.LEFT;
        } else if (currentPos.x - lastPos.x == 0
                && currentPos.y - lastPos.y > 0
                && Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.currentDirection = Directions.UP;
        } else if (currentPos.x - lastPos.x == 0
                && currentPos.y - lastPos.y < 0
                && Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.currentDirection = Directions.DOWN;
        } else if (currentPos.x - lastPos.x > 0
                && currentPos.y - lastPos.y > 0
                && Gdx.input.isKeyPressed(Input.Keys.W)
                && Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.currentDirection = Directions.UP_RIGHT;
        } else if (currentPos.x - lastPos.x < 0
                && currentPos.y - lastPos.y < 0
                && Gdx.input.isKeyPressed(Input.Keys.S)
                && Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.currentDirection = Directions.DOWN_LEFT;
        } else if (currentPos.x - lastPos.x > 0
                && currentPos.y - lastPos.y < 0
                && Gdx.input.isKeyPressed(Input.Keys.S)
                && Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.currentDirection = Directions.DOWN_RIGHT;
        } else if (currentPos.x - lastPos.x < 0
                && currentPos.y - lastPos.y > 0
                && Gdx.input.isKeyPressed(Input.Keys.W)
                && Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.currentDirection = Directions.UP_LEFT;
        }
    }

    /**
     * Render player.
     *
     * @param batch sprite batch
     */
    public void render(SpriteBatch batch) {
        batch.draw(currentTexture, position.x, position.y, playerWidth, playerHeight);
    }

    /**
     * Dispose.
     */
    public void dispose() {
        textureRed.dispose();
        textureGreen.dispose();
    }

    /**
     * Get current player texture.
     *
     * @return current player texture
     */
    public Texture getCurrentTexture() {
        return currentTexture;
    }
}
