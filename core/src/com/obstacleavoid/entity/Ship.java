package com.obstacleavoid.entity;

import com.obstacleavoid.config.Constants;


public class Ship extends BaseGameObj {

    public Ship() {
        super(Constants.PLAYER_BOUNDS_RADIUS);
        setSize(Constants.PLAYER_SIZE, Constants.PLAYER_SIZE);
    }
    
}
