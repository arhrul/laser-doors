package com.mygdx.game.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.LaserDoorsGame;
import com.mygdx.game.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameScreen implements Screen {
    private LaserDoorsGame game;

    private float screenWidth, screenHeight;

    private SpriteBatch batch;
    private BitmapFont font;

    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    private String mapPath;

    private Player player;

//    private Texture playerCurrentTexture;
    private Texture hudTexture = new Texture("hud.png");
    private float hudWidth, hudHeight;
    private float hudX, hudY;

    private String timerText;
    private float timerTextWidth;
    private float timerTextHeight;

    private Vector2 playerSpawn;

    private float playerX, playerY;

    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Array<Rectangle> collisionRectangles = new Array<>();

    private Array<Rectangle> redDoorsRectangles = new Array<>();
    private Array<Rectangle> greenDoorsRectangles = new Array<>();

    private Array<Rectangle> redFinishRectangles = new Array<>();
    private Array<Rectangle> greenFinishRectangles = new Array<>();
    private Array<Rectangle> standardFinishRectangles = new Array<>();

    private Array<Rectangle> horizontalDoorsRectangles = new Array<>();
    private Array<Rectangle> verticalDoorsRectangles = new Array<>();

    private static List<String> levels = new ArrayList<>(Arrays.asList(
            "tiled/map1.tmx",
            "tiled/map2.tmx",
            "tiled/map3.tmx",
            "tiled/map4.tmx",
            "tiled/map5.tmx"
    ));

    private static int currentLevel = 0;

    float timePassed = 0f;

    private Texture backgroundTexture = new Texture("background.jpg");

    private GlyphLayout layout = new GlyphLayout();

    /**
     * Game screen constructor.
     *
     * @param game    game
     * @param mapPath path to map
     */
    public GameScreen(LaserDoorsGame game, String mapPath) {
        this.game = game;

        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.batch = new SpriteBatch();
        this.mapPath = mapPath;

        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/RadiantKingdom-mL5eV.ttf"));
        this.parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        this.parameter.color = Color.WHITE;
        this.parameter.size = 40;
        this.font = generator.generateFont(parameter);

        this.hudWidth = this.hudTexture.getWidth();
        this.hudHeight = this.hudTexture.getHeight();
        this.hudX = this.screenWidth / 2 - hudWidth / 2;
        this.hudY = this.screenHeight - hudHeight;

//        this.playerCurrentTexture = new Texture(this.game.getSkinSettings().getStandardCurrentSkinPath());
    }

    /**
     * Get current level.
     *
     * @return index of level
     */
    public int getCurrentLevel() {
        if (mapPath.equals("tiled/map1.tmx")) {
            return 0;
        }
        if (mapPath.equals("tiled/map2.tmx")) {
            return 1;
        }
        if (mapPath.equals("tiled/map3.tmx")) {
            return 2;
        }
        if (mapPath.equals("tiled/map4.tmx")) {
            return 3;
        }
        return 4;
    }

    /**
     * Change player texture.
     *
     * @param playerCurrentTexture new player texture
     */
//    public void setPlayerCurrentTexture(Texture playerCurrentTexture) {
//        this.playerCurrentTexture = playerCurrentTexture;
//    }

    /**
     * Get all levels.
     *
     * @return levels
     */
    public static List<String> getLevels() {
        return levels;
    }

    /**
     * Update player positsion.
     *
     * @param x x
     * @param y y
     */
    public void updatePlayerPosition(float x, float y) {
        this.playerX = x;
        this.playerY = y;
    }

    @Override
    public void show() {
        this.map = new TmxMapLoader().load(mapPath);
        this.mapRenderer = new OrthogonalTiledMapRenderer(map);

        int mapWidthInTiles = map.getProperties().get("width", Integer.class);
        int mapHeightInTiles = map.getProperties().get("height", Integer.class);
        int tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);
        int tilePixelHeight = map.getProperties().get("tileheight", Integer.class);

        float mapWidth = mapWidthInTiles * tilePixelWidth;
        float mapHeight = mapHeightInTiles * tilePixelHeight;

        float mapCenterX = mapWidth / 2f;
        float mapCenterY = mapHeight / 2f;

        float screenCenterX = Gdx.graphics.getWidth() / 2f;
        float screenCenterY = Gdx.graphics.getHeight() / 2f;

        float offsetX = screenCenterX - mapCenterX;
        float offsetY = screenCenterY - mapCenterY;

        camera.position.set(mapCenterX, mapCenterY, 0);
        camera.update();

        this.playerSpawn = new Vector2(
                ((RectangleMapObject) map.getLayers().get("spawn").getObjects().get(0)).getRectangle().x + offsetX,
                ((RectangleMapObject) map.getLayers().get("spawn").getObjects().get(0)).getRectangle().y + offsetY
        );

        if (playerX == 0 || playerY == 0) {
            playerX = playerSpawn.x;
            playerY = playerSpawn.y;
        }

        this.player = new Player(this, playerX, playerY, 200f);

        loadMapObjects(offsetX, offsetY);
    }

    /**
     * Load objects from the map.
     *
     * @param offsetX
     * @param offsetY
     */
    private void loadMapObjects(float offsetX, float offsetY) {
        loadObjectsWithOffset(map.getLayers().get("collisions"), collisionRectangles, offsetX, offsetY);
        loadObjectsWithOffset(map.getLayers().get("red-doors"), redDoorsRectangles, offsetX, offsetY);
        loadObjectsWithOffset(map.getLayers().get("green-doors"), greenDoorsRectangles, offsetX, offsetY);
        loadObjectsWithOffset(map.getLayers().get("red-finish"), redFinishRectangles, offsetX, offsetY);
        loadObjectsWithOffset(map.getLayers().get("green-finish"), greenFinishRectangles, offsetX, offsetY);
        loadObjectsWithOffset(map.getLayers().get("standard-finish"), standardFinishRectangles, offsetX, offsetY);
        loadObjectsWithOffset(map.getLayers().get("horizontal-doors"), horizontalDoorsRectangles, offsetX, offsetY);
        loadObjectsWithOffset(map.getLayers().get("vertical-doors"), verticalDoorsRectangles, offsetX, offsetY);
    }

    /**
     * Loads rectangle objects from the specified map layer.
     *
     * @param layer          the map layer from which to load rectangle objects
     * @param rectangleArray the array to which the rectangles will be added
     * @param offsetX        the offset to apply to the x-coordinate of each rectangle
     * @param offsetY        the offset to apply to the y-coordinate of each rectangle
     */
    private void loadObjectsWithOffset(MapLayer layer, Array<Rectangle> rectangleArray, float offsetX, float offsetY) {
        if (layer != null) {
            for (MapObject object : layer.getObjects()) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle rect = rectangleObject.getRectangle();
                    rect.setPosition(rect.x + offsetX, rect.y + offsetY);
                    rectangleArray.add(rect);
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timePassed += Gdx.graphics.getDeltaTime();

        this.timerText = "Time: " + Math.round(timePassed * 10.0) / 10.0;
        layout.setText(font, timerText);
        timerTextWidth = layout.width;
        timerTextHeight = layout.height;

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        player.update(delta);
        player.render(batch);
        batch.draw(new Texture("HUD.png"), hudX, hudY);
        font.draw(batch, timerText, screenWidth / 2 - timerTextWidth / 2, screenHeight - 20);
        batch.end();

//        this.setPlayerCurrentTexture(player.getCurrentTexture());

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new Pause(game, this));
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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
        font.dispose();
//        playerCurrentTexture.dispose();
        backgroundTexture.dispose();
        map.dispose();
        player.dispose();
    }

    /**
     * Get collision rectangles.
     *
     * @return collision rectangles
     */
    public Array<Rectangle> getCollisionRectangles() {
        return collisionRectangles;
    }

    /**
     * Get green doors rectangles.
     *
     * @return green doors rectangles
     */
    public Array<Rectangle> getGreenDoorsRectangles() {
        return greenDoorsRectangles;
    }

    /**
     * Get red doors rectangles.
     *
     * @return red doors rectangles
     */
    public Array<Rectangle> getRedDoorsRectangles() {
        return redDoorsRectangles;
    }

    /**
     * Get green finish rectangles.
     *
     * @return green finish rectangles
     */
    public Array<Rectangle> getGreenFinishRectangles() {
        return greenFinishRectangles;
    }

    /**
     * Get red finish rectangles.
     *
     * @return red finish rectangles
     */
    public Array<Rectangle> getRedFinishRectangles() {
        return redFinishRectangles;
    }

    /**
     * Get standard finish rectangles.
     *
     * @return standard finish rectangles
     */
    public Array<Rectangle> getStandardFinishRectangles() {
        return standardFinishRectangles;
    }

    /**
     * Get horizontal doors rectangles.
     *
     * @return horizontal doors rectangles
     */
    public Array<Rectangle> getHorizontalDoorsRectangles() {
        return horizontalDoorsRectangles;
    }

    /**
     * Get vertical doors rectangles.
     *
     * @return vertical doors rectangles
     */
    public Array<Rectangle> getVerticalDoorsRectangles() {
        return verticalDoorsRectangles;
    }

    /**
     * Get map path.
     *
     * @return map path
     */
    public String getMapPath() {
        return mapPath;
    }

    /**
     * Get time passed.
     *
     * @return time passed
     */
    public float getTimePassed() {
        return timePassed;
    }

    /**
     * Get game.
     *
     * @return game
     */
    public LaserDoorsGame getGame() {
        return game;
    }
}
