package com.obstacleavoid.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacleavoid.font_res.Fonts;
import com.obstacleavoid.config.Constants;
import com.obstacleavoid.entity.Background;
import com.obstacleavoid.entity.Meteorite;
import com.obstacleavoid.entity.Ship;
import com.obstacleavoid.util.ScreenUtils;
import com.obstacleavoid.util.ViewportUtils;
import com.obstacleavoid.util.debug.DebugCameraController;


public class GameRenderer implements Disposable {

    // == attributes ==
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private OrthographicCamera hudCamera;
    private Viewport hudViewport;

    private SpriteBatch batch;
    private BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();
    private DebugCameraController debugCameraController;
    private final GameLogic controller;
    private Texture playerTexture;
    private Texture obstacleTexture;
    private Texture backgroundTexture;

    // == constructors ==
    public GameRenderer(GameLogic controller) {
        this.controller = controller;
        init();
    }

    // == init ==
    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(Constants.HUD_WIDTH, Constants.HUD_HEIGHT, hudCamera);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal(Fonts.UI_FONT));

        // create debug camera controller
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(Constants.WORLD_CENTER_X, Constants.WORLD_CENTER_Y);

        playerTexture = new Texture(Gdx.files.internal("gameplay/spaceship.png"));
        obstacleTexture = new Texture(Gdx.files.internal("gameplay/meteo.png"));
        backgroundTexture = new Texture(Gdx.files.internal("gameplay/back.png"));
    }

    // == public methods ==
    public void render(float delta) {
        // not wrapping inside alive cuz we want to be able to control camera even when there is game over
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        if(Gdx.input.isTouched() && !controller.isGameOver()) {
            Vector2 screenTouch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 worldTouch = viewport.unproject(new Vector2(screenTouch));

            System.out.println("screenTouch= " + screenTouch);
            System.out.println("worldTouch= " + worldTouch);

            Ship ship = controller.getShip();
            worldTouch.x = MathUtils.clamp(worldTouch.x, 0, Constants.WORLD_WIDTH - ship.getWidth());
            ship.setX(worldTouch.x);
        }

        // clear screen
        ScreenUtils.clearScreen();

        renderGamePlay();

        // render ui/hud
        renderUi();

        // render debug graphics
//        renderDebug();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        batch.dispose();
        font.dispose();
        playerTexture.dispose();
        obstacleTexture.dispose();
        backgroundTexture.dispose();
    }

    // == private methods ==
    private void renderGamePlay() {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // draw background
        Background background = controller.getBackground();
        batch.draw(backgroundTexture,
                background.getX(), background.getY(),
                background.getWidth(), background.getHeight()
        );

        // draw ship
        Ship ship = controller.getShip();
        batch.draw(playerTexture,
                ship.getX(), ship.getY(),
                ship.getWidth(), ship.getHeight()
        );

        // draw obstacles
        for (Meteorite meteorite : controller.getObstacles()) {
            batch.draw(obstacleTexture,
                    meteorite.getX(), meteorite.getY(),
                    meteorite.getWidth(), meteorite.getHeight()
            );
        }

        batch.end();
    }

    private void renderUi() {
        hudViewport.apply();
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        String livesText = "LIVES: " + controller.getLives();
        layout.setText(font, livesText);

        font.draw(batch, livesText,
                20,
                Constants.HUD_HEIGHT - layout.height
        );

        String scoreText = "SCORE: " + controller.getDisplayScore();
        layout.setText(font, scoreText);

        font.draw(batch, scoreText,
                Constants.HUD_WIDTH - layout.width - 20,
                Constants.HUD_HEIGHT - layout.height
        );

        batch.end();
    }

    private void renderDebug() {
        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();

        renderer.end();

        ViewportUtils.drawGrid(viewport, renderer);
    }

    private void drawDebug() {
        Ship ship = controller.getShip();
        ship.drawDebug(renderer);

        Array<Meteorite> obstacles = controller.getObstacles();

        for (Meteorite meteorite : obstacles) {
            meteorite.drawDebug(renderer);
        }
    }
}
