package com.me4502.SolarTennis.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me4502.SolarTennis.SolarTennis;
import com.me4502.SolarTennis.UpdateThread;
import com.me4502.SolarTennis.entities.components.GravityComponent;
import com.me4502.SolarTennis.simulation.GravityUtil;

import java.util.ArrayList;
import java.util.List;

public class Entity {
	private int entityId;

	public float x;
	public float y;
	private float motionX, motionY;

	public Entity(int entityId, float x, float y, float mass) {
		this.entityId = entityId;
		this.x = x;
		this.y = y;
		motionX = 0;
		motionY = 0;

		SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).set(entityId, mass);
	}

	public int getId() {
		return entityId;
	}

	private int timeSinceCrash = 25;

	public void update() {
		if(SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).get(entityId) < 0.1f) {
			SolarTennis.tennis.entities.remove(this);
			UpdateThread.scheduleUpdate();
			return;
		}

		timeSinceCrash ++;

		x += motionX;
		y += motionY;

		List<Entity> collidingEntities = new ArrayList<>();

		for(Entity ent : UpdateThread.getSynchronizedEntityList()) {

			if(ent == this) continue;

			float distance = GravityUtil.pow2(ent.x - x) + GravityUtil.pow2(ent.y - y);
			if(distance < GravityUtil.pow2(SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).get(ent.entityId) + SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).get(entityId))) {
				collidingEntities.add(ent);
			}
		}

		if(collidingEntities.size() > 0) {
			for(Entity ent : collidingEntities) {
				if(timeSinceCrash > 10) {
					motionX -= motionX*(SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).get(ent.entityId)/ SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).get(entityId));
					motionY -= motionY*(SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).get(ent.entityId)/ SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).get(entityId));
					timeSinceCrash = 0;
				}
			}
		}

		if(x < -Gdx.graphics.getWidth()/2) {
			x ++;
			motionX = 0;
		}
		if(x > Gdx.graphics.getWidth()/2) {
			x --;
			motionX = 0;
		}

		if(y < -Gdx.graphics.getHeight()/2) {
			y ++;
			motionY = 0;
		}
		if(y > Gdx.graphics.getHeight()/2) {
			y --;
			motionY = 0;
		}
	}

	public void updateGravity() {
		float mx = 0f, my = 0f;

		for(int xx = -1; xx < 2; xx++)
			for(int yy = -1; yy < 2; yy++) {

				if(yy == 0 && xx == 0) continue;

				try {
					float m = GravityUtil.getGravityAtWithout(x+xx,y+yy, this);

					mx += xx*m;
					my += yy*m;

				} catch(Exception e){
					e.printStackTrace();
				}
			}

		if(mx != 0 || my != 0) {
			motionX += mx;
			motionY += my;
		}
	}

	public void render(SpriteBatch batch) {
		SolarTennis.tennis.shapes.setProjectionMatrix(SolarTennis.tennis.camera.combined);
		SolarTennis.tennis.shapes.begin(ShapeType.Filled);

		SolarTennis.tennis.shapes.setColor(Color.WHITE);

		SolarTennis.tennis.shapes.circle(x, y, SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).get(entityId));

		SolarTennis.tennis.shapes.end();
	}
}