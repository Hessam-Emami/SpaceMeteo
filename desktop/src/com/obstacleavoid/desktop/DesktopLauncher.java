package com.obstacleavoid.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.obstacleavoid.SpaceMeteoGame;
import com.obstacleavoid.config.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) Constants.WIDTH;
		config.height = (int) Constants.HEIGHT;
		new LwjglApplication(new SpaceMeteoGame(), config);
	}
}
