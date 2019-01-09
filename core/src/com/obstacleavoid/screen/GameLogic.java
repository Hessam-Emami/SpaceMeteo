package com.obstacleavoid.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.obstacleavoid.config.DifficultyLevel;
import com.obstacleavoid.config.Constants;
import com.obstacleavoid.entity.Background;
import com.obstacleavoid.entity.Meteorite;
import com.obstacleavoid.entity.Ship;


public class GameLogic {

    // == constants ==
    private static final Logger log = new Logger(GameLogic.class.getName(), Logger.DEBUG);

    // == attributes ==
    private Ship ship;
    private Array<Meteorite> obstacles = new Array<Meteorite>();
    private Background background;
    private float obstacleTimer;
    private float scoreTimer;
    private int lives = Constants.LIVES_START;
    private int score;
    private int displayScore;
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;
    private Pool<Meteorite> obstaclePool;
    private final float startPlayerX = (Constants.WORLD_WIDTH - Constants.PLAYER_SIZE) / 2f;
    private final float startPlayerY = 1 - Constants.PLAYER_SIZE / 2f;


    // == constructors ==
    public GameLogic() {
        init();
    }

    // == init ==
    private void init() {
        // create ship
        ship = new Ship();
        ship.setPosition(startPlayerX, startPlayerY);

        // create obstacle pool
        obstaclePool = Pools.get(Meteorite.class, 40);

        background = new Background();
        background.setPosition(0, 0);
        background.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
    }

    // == public methods ==
    public void update(float delta) {
        if (isGameOver()) {
            return;
        }

        updatePlayer();
        updateObstacles(delta);
        updateScore(delta);
        updateDisplayScore(delta);

        if (isPlayerCollidingWithObstacle()) {
            log.debug("Collision detected.");
            lives--;

            if (isGameOver()) {
                log.debug("Game Over!!!");
            } else {
                restart();
            }
        }
    }

    public Ship getShip() {
        return ship;
    }

    public Array<Meteorite> getObstacles() {
        return obstacles;
    }

    public Background getBackground() {
        return background;
    }

    public int getLives() {
        return lives;
    }

    public int getDisplayScore() {
        return displayScore;
    }


    public boolean isGameOver() {
        return lives <= 0;
    }

    // == private methods ==
    private void restart() {
        obstaclePool.freeAll(obstacles);
        obstacles.clear();
        ship.setPosition(startPlayerX, startPlayerY);
    }

    private boolean isPlayerCollidingWithObstacle() {
        for (Meteorite meteorite : obstacles) {
            if (meteorite.isNotHit() && meteorite.isPlayerColliding(ship)) {
                return true;
            }
        }

        return false;
    }

    private void updatePlayer() {
        float xSpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = Constants.MAX_PLAYER_X_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -Constants.MAX_PLAYER_X_SPEED;
        }

        ship.setX(ship.getX() + xSpeed);

        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {
        float playerX = MathUtils.clamp(
                ship.getX(),
                0,
                Constants.WORLD_WIDTH - ship.getWidth()
        );

        ship.setPosition(playerX, ship.getY());
    }

    private void updateObstacles(float delta) {
        for (Meteorite meteorite : obstacles) {
            meteorite.update();
        }

        createNewObstacle(delta);
        removePassedObstacles();
    }

    private void createNewObstacle(float delta) {
        obstacleTimer += delta;

        if (obstacleTimer >= Constants.OBSTACLE_SPAWN_TIME) {
            float min = 0;
            float max = Constants.WORLD_WIDTH - Constants.OBSTACLE_SIZE;

            float obstacleX = MathUtils.random(min, max);
            float obstacleY = Constants.WORLD_HEIGHT;

            Meteorite meteorite = obstaclePool.obtain();
            meteorite.setYSpeed(difficultyLevel.getObstacleSpeed());
            meteorite.setPosition(obstacleX, obstacleY);

            obstacles.add(meteorite);
            obstacleTimer = 0f;
        }
    }

    private void removePassedObstacles() {
        if (obstacles.size > 0) {
            Meteorite first = obstacles.first();

            float minObstacleY = -Constants.OBSTACLE_SIZE;

            if (first.getY() < minObstacleY) {
                obstacles.removeValue(first, true);
                obstaclePool.free(first);
            }
        }
    }

    private void updateScore(float delta) {
        scoreTimer += delta;

        if (scoreTimer >= Constants.SCORE_MAX_TIME) {
            score += MathUtils.random(1, 5);
            scoreTimer = 0.0f;
        }
    }

    private void updateDisplayScore(float delta) {
        if (displayScore < score) {
            displayScore = Math.min(
                    score,
                    displayScore + (int) (40 * delta)
            );
        }
    }

}
