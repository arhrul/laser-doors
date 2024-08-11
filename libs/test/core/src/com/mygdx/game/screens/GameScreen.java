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
    private final LaserDoorsGame game;

    private final float screenWidth;
    private final float screenHeight;

    private final SpriteBatch batch;
    private final BitmapFont font;

    private final FreeTypeFontGenerator generator;
    private final FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    private final String mapPath;

    private Player player;

    private final Texture hudTexture = new Texture("hud.png");
    private final float hudWidth, hudHeight;
    private final float hudX, hudY;

    private String timerText;
    private float timerTextWidth;
    private float timerTextHeight;

    private Vector2 playerSpawn;

    private float playerX, playerY;

    private final OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private final Array<Rectangle> collisionRectangles = new Array<>();

    private final Array<Rectangle> redDoorsRectangles = new Array<>();
    private final Array<Rectangle> greenDoorsRectangles = new Array<>();

    private final Array<Rectangle> redFinishRectangles = new Array<>();
    private final Array<Rectangle> greenFinishRectangles = new Array<>();
    private final Array<Rectangle> standardFinishRectangles = new Array<>();

    private final Array<Rectangle> horizontalDoorsRectangles = new Array<>();
    private final Array<Rectangle> verticalDoorsRectangles = new Array<>();

    private static final List<String> levels = new ArrayList<>(Arrays.asList(
            "tiled/map1.tmx",
            "tiled/map2.tmx",
            "tiled/map3.tmx",
            "tiled/map4.tmx",
            "tiled/map5.tmx"
    ));

    private float timePassed = 0f;

    private final Texture backgroundTexture = new Texture("background.jpg");

    private final GlyphLayout layout = new GlyphLayout();

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

        this.camera.position.set(mapCenterX, mapCenterY, 0);
        this.camera.update();

        this.playerSpawn = new Vector2(
                ((RectangleMapObject) this.map.getLayers().get("spawn")
                        .getObjects().get(0)).getRectangle().x + offsetX,
                ((RectangleMapObject) this.map.getLayers().get("spawn")
                        .getObjects().get(0)).getRectangle().y + offsetY
        );

        if (this.playerX == 0 || playerY == 0) {
            this.playerX = this.playerSpawn.x;
            this.playerY = this.playerSpawn.y;
        }

        this.player = new Player(this, playerX, playerY, 200f);

        loadMapObjects(offsetX, offsetY);
    }

    /**
     * Load objects from the map.
     *
     * @param offsetX offsetX
     * @param offsetY offsetY
     */
    private void loadMapObjects(float offsetX, float offsetY) {
        loadObjectsWithOffset(this.map.getLayers().get("collisions"), this.collisionRectangles, offsetX, offsetY);
        loadObjectsWithOffset(this.map.getLayers().get("red-doors"), this.redDoorsRectangles, offsetX, offsetY);
        loadObjectsWithOffset(this.map.getLayers().get("green-doors"), this.greenDoorsRectangles, offsetX, offsetY);
        loadObjectsWithOffset(this.map.getLayers().get("red-finish"), this.redFinishRectangles, offsetX, offsetY);
        loadObjectsWithOffset(this.map.getLayers().get("green-finish"), this.greenFinishRectangles, offsetX, offsetY);
        loadObjectsWithOffset(this.map.getLayers().get("standard-finish"),
                this.standardFinishRectangles, offsetX, offsetY);
        loadObjectsWithOffset(this.map.getLayers().get("horizontal-doors"),
                this.horizontalDoorsRectangles, offsetX, offsetY);
        loadObjectsWithOffset(this.map.getLayers().get("vertical-doors"),
                this.verticalDoorsRectangles, offsetX, offsetY);
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

        this.timePassed += Gdx.graphics.getDeltaTime();

        this.timerText = "Time: " + Math.round(this.timePassed * 10.0) / 10.0;
        this.layout.setText(this.font, this.timerText);
        this.timerTextWidth = this.layout.width;
        this.timerTextHeight = this.layout.height;

        this.batch.begin();
        this.batch.draw(this.backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.batch.end();

        this.camera.update();
        this.mapRenderer.setView(this.camera);
        this.mapRenderer.render();

        this.batch.begin();
        this.player.update(delta);
        this.player.render(this.batch);
        this.batch.draw(new Texture("HUD.png"), this.hudX, this.hudY);
        this.font.draw(this.batch, this.timerText, this.screenWidth / 2 - this.timerTextWidth / 2, this.screenHeight - 20);
        this.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.game.setScreen(new Pause(this.game, this));
        }
    }

    @Override
    public void resize(int width, int height) {
        this.camera.viewportWidth = width;
        this.camera.viewportHeight = height;
        this.camera.update();
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
        this.font.dispose();
        this.backgroundTexture.dispose();
        this.map.dispose();
        this.player.dispose();
    }

    /**
     * Get collision rectangles.
     *
     * @return collision rectangles
     */
    public Array<Rectangle> getCollisionRectangles() {
        return this.collisionRectangles;
    }

    /**
     * Get green doors rectangles.
     *
     * @return green doors rectangles
     */
    public Array<Rectangle> getGreenDoorsRectangles() {
        return this.greenDoorsRectangles;
    }

    /**
     * Get red doors rectangles.
     *
     * @return red doors rectangles
     */
    public Array<Rectangle> getRedDoorsRectangles() {
        return this.redDoorsRectangles;
    }

    /**
     * Get green finish rectangles.
     *
     * @return green finish rectangles
     */
    public Array<Rectangle> getGreenFinishRectangles() {
        return this.greenFinishRectangles;
    }

    /**
     * Get red finish rectangles.
     *
     * @return red finish rectangles
     */
    public Array<Rectangle> getRedFinishRectangles() {
        return this.redFinishRectangles;
    }

    /**
     * Get standard finish rectangles.
     *
     * @return standard finish rectangles
     */
    public Array<Rectangle> getStandardFinishRectangles() {
        return this.standardFinishRectangles;
    }

    /**
     * Get horizontal doors rectangles.
     *
     * @return horizontal doors rectangles
     */
    public Array<Rectangle> getHorizontalDoorsRectangles() {
        return this.horizontalDoorsRectangles;
    }

    /**
     * Get vertical doors rectangles.
     *
     * @return vertical doors rectangles
     */
    public Array<Rectangle> getVerticalDoorsRectangles() {
        return this.verticalDoorsRectangles;
    }

    /**
     * Get map path.
     *
     * @return map path
     */
    public String getMapPath() {
        return this.mapPath;
    }

    /**
     * Get time passed.
     *
     * @return time passed
     */
    public float getTimePassed() {
        return this.timePassed;
    }

    /**
     * Get game.
     *
     * @return game
     */
    public LaserDoorsGame getGame() {
        return this.game;
    }
}
