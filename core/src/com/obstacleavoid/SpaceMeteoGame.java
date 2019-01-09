package com.obstacleavoid;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.obstacleavoid.screen.GameScreen;
import com.obstacleavoid.util.ScreenUtils;

public class SpaceMeteoGame extends Game {
	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		ScreenUtils.clearScreen();
		setScreen(new GameScreen());

	}
}
