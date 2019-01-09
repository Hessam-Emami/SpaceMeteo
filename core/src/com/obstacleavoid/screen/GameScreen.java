package com.obstacleavoid.screen;

import com.badlogic.gdx.Screen;


public class GameScreen implements Screen {

    private GameLogic controller;
    private GameRenderer renderer;

    @Override
    public void show() {
        controller = new GameLogic();
        renderer = new GameRenderer(controller);
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
