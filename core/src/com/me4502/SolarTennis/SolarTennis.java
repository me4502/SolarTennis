package com.me4502.SolarTennis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.me4502.SolarTennis.entities.Entity;
import com.me4502.SolarTennis.simulation.GravityUtil;

import java.util.concurrent.ThreadLocalRandom;

public class SolarTennis extends ApplicationAdapter implements InputProcessor {

    public OrthographicCamera camera;

    public static SolarTennis tennis;

    public ShapeRenderer shapes;

    public Array<Entity> entities = new Array<>(Entity.class);

    private RenderMode renderMode;

    @Override
    public void create() {
        tennis = this;

        shapes = new ShapeRenderer();

        new UpdateThread().start();

        for (int i = 0; i < 500; i++) {
            entities.add(new Entity(i, -Gdx.graphics.getWidth() / 2 + ThreadLocalRandom.current().nextInt(Gdx.graphics.getWidth()), -Gdx.graphics.getHeight() / 2 + ThreadLocalRandom.current().nextInt(Gdx.graphics.getHeight()), 1f));
        }

        renderMode = RenderMode.VIEW_CIRCLE;

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void dispose() {
        super.dispose();

        UpdateThread.disable();
    }

    private int viewSize = 25;

    @Override
    public void resize(int width, int height) {
        camera = new OrthographicCamera(width, height);
        camera.translate(-width * 2, -height * 2);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeType.Point);

        if (renderMode == RenderMode.VIEW_CIRCLE) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT_BRACKET))
                viewSize++;
            else if (Gdx.input.isKeyPressed(Input.Keys.LEFT_BRACKET))
                viewSize--;

            viewSize = Math.max(1, viewSize);

            for (int x = Gdx.input.getX() - viewSize; x < Gdx.graphics.getWidth() && x < Gdx.input.getX() + viewSize; x++) {
                for (int y = Gdx.graphics.getHeight() - Gdx.input.getY() - viewSize; y < Gdx.graphics.getHeight() && y < Gdx.graphics.getHeight() - Gdx.input.getY() + viewSize; y++) {
                    if (GravityUtil.pow2(x - Gdx.input.getX()) + GravityUtil.pow2(y - (Gdx.graphics.getHeight() - Gdx.input.getY())) > viewSize * viewSize)
                        continue;
                    float f = GravityUtil.getGravityAt(x - Gdx.graphics.getWidth() / 2, y - Gdx.graphics.getHeight() / 2);

                    f *= 10000;

                    shapes.setColor(f / 255f, f / 255f, f / 255f, 1.0f);
                    shapes.point(x - Gdx.graphics.getWidth() / 2, y - Gdx.graphics.getHeight() / 2, 0);
                }
            }
        } else if (renderMode == RenderMode.SCREEN) {
            for (int x = 0; x < Gdx.graphics.getWidth(); x++) {
                for (int y = 0; y < Gdx.graphics.getHeight(); y++) {
                    float f = GravityUtil.getGravityAt(x - Gdx.graphics.getWidth() / 2, y - Gdx.graphics.getHeight() / 2);

                    f *= 10000;

                    shapes.setColor(f / 255f, f / 255f, f / 255f, 1.0f);
                    shapes.point(x - Gdx.graphics.getWidth() / 2, y - Gdx.graphics.getHeight() / 2, 0);
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            if (renderMode == RenderMode.SCREEN)
                renderMode = RenderMode.NONE;
            else if (renderMode == RenderMode.NONE)
                renderMode = RenderMode.VIEW_CIRCLE;
            else if (renderMode == RenderMode.VIEW_CIRCLE)
                renderMode = RenderMode.SCREEN;
        }

        shapes.end();

        shapes.begin(ShapeType.Filled);
        shapes.setColor(Color.WHITE);

        for (Entity ent : entities)
            ent.render();

        shapes.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (renderMode == RenderMode.VIEW_CIRCLE) {
            viewSize -= amount;

            return true;
        }

        return false;
    }

    private enum RenderMode {
        VIEW_CIRCLE, SCREEN, NONE
    }
}
