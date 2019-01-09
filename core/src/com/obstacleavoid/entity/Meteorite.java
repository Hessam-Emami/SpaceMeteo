package com.obstacleavoid.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Pool;
import com.obstacleavoid.config.Constants;


public class Meteorite extends BaseGameObj implements Pool.Poolable {

    private float ySpeed = Constants.MEDIUM_OBSTACLE_SPEED;
    private boolean hit;

    public Meteorite() {
        super(Constants.OBSTACLE_BOUNDS_RADIUS);
        setSize(Constants.OBSTACLE_SIZE, Constants.OBSTACLE_SIZE);
    }

    public void update() {
        setY(getY() - ySpeed);
    }

    public boolean isPlayerColliding(Ship ship) {
        Circle playerBounds = ship.getBounds();
        boolean overlaps = Intersector.overlaps(playerBounds, getBounds());

//        if(overlaps) {
//            hit = true;
//        }

        hit = overlaps;

        return overlaps;
    }

    public boolean isNotHit() {
        return !hit;
    }

    public void setYSpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    @Override
    public void reset() {
        hit = false;
    }
}
