package com.obstacleavoid.config;


public enum DifficultyLevel {
    EASY(Constants.EASY_OBSTACLE_SPEED),
    MEDIUM(Constants.MEDIUM_OBSTACLE_SPEED),
    HARD(Constants.HARD_OBSTACLE_SPEED);

    private final float obstacleSpeed;

    DifficultyLevel(float obstacleSpeed) {
        this.obstacleSpeed = obstacleSpeed;
    }

    public float getObstacleSpeed() {
        return obstacleSpeed;
    }


}
