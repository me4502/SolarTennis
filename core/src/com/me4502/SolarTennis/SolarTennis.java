package com.me4502.SolarTennis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me4502.SolarTennis.entities.Entity;
import com.me4502.SolarTennis.entities.components.ComponentManager;
import com.me4502.SolarTennis.entities.components.GravityComponent;
import com.me4502.SolarTennis.simulation.GravityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SolarTennis extends ApplicationAdapter {

	public OrthographicCamera camera;

	public static SolarTennis tennis;

	private SpriteBatch batch;
	private BitmapFont font;
	public ShapeRenderer shapes;

	public List<Entity> entities = new ArrayList<>();

	public ComponentManager componentManager = new ComponentManager();

	@Override
	public void create () {

		tennis = this;

		componentManager.registerComponent(new GravityComponent());

		batch = new SpriteBatch();

		font = new BitmapFont();
		font.setColor(Color.RED);

		shapes = new ShapeRenderer();

		new UpdateThread().start();

		for(int i = 0; i < 100; i++) {
			entities.add(new Entity(i, -Gdx.graphics.getWidth()/2 + ThreadLocalRandom.current().nextInt(Gdx.graphics.getWidth()), -Gdx.graphics.getHeight()/2 + ThreadLocalRandom.current().nextInt(Gdx.graphics.getHeight()), 1f));
		}
	}

	private int viewSize = 25;

	@Override
	public void resize (int width, int height) {

		camera = new OrthographicCamera(width, height);
		camera.translate(-width*2, -height*2);

		UpdateThread.scheduleUpdate();
	}

	@Override
	public void render () {

		if(Gdx.input.isKeyPressed(Keys.RIGHT_BRACKET))
			viewSize ++;
		else if(Gdx.input.isKeyPressed(Keys.LEFT_BRACKET))
			viewSize --;

		viewSize = Math.max(1,viewSize);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapes.setProjectionMatrix(camera.combined);
		shapes.begin(ShapeType.Point);

		float cursorPoint = 0f;

		for(int x = Gdx.input.getX()-viewSize; x < Gdx.graphics.getWidth() && x < Gdx.input.getX()+viewSize; x++) {
			for(int y = Gdx.graphics.getHeight()-Gdx.input.getY()-viewSize; y < Gdx.graphics.getHeight() && y < Gdx.graphics.getHeight()-Gdx.input.getY()+viewSize; y++) {

				if(GravityUtil.pow2(x - Gdx.input.getX()) + GravityUtil.pow2(y - (Gdx.graphics.getHeight()-Gdx.input.getY())) > viewSize*viewSize) continue;

				float f = GravityUtil.getGravityAt(x-Gdx.graphics.getWidth()/2,y-Gdx.graphics.getHeight()/2);

				if(x == Gdx.input.getX() && y == Gdx.graphics.getHeight()-Gdx.input.getY()) {
					cursorPoint = f;
					continue;
				}

				f *= 10000;

				shapes.setColor(f/255f, f/255f, f/255f, 1.0f);
				shapes.point(x-Gdx.graphics.getWidth()/2, y-Gdx.graphics.getHeight()/2, 0);
			}
		}

		shapes.end();

		for(Entity ent : UpdateThread.getSynchronizedEntityList())
			ent.render(batch);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		try {
			font.draw(batch, "Grav: " + cursorPoint, -Gdx.graphics.getWidth()/2 + 50, -Gdx.graphics.getHeight()/2 + 50);
		} catch(Exception e){
			e.printStackTrace();
		}
		batch.end();
	}
}
