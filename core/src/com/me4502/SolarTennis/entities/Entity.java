package com.me4502.SolarTennis.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me4502.SolarTennis.SolarTennis;
import com.me4502.SolarTennis.UpdateThread;
import com.me4502.SolarTennis.entities.components.GravityComponent;
import com.me4502.SolarTennis.simulation.GravityUtil;

public class Entity {

	public int entityId;

	public float x;
	public float y;
	float motionX, motionY;

	public Entity(int entityId, float x, float y, float mass) {

		this.entityId = entityId;
		this.x = x;
		this.y = y;
		motionX = 0;
		motionY = 0;

		((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).set(entityId, mass);
	}

	public int getId() {

		return entityId;
	}

	int timeSinceCrash = 25;

	public void update() {

		if(((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(getId()) < 0.1f) {
			SolarTennis.tennis.entities.remove(this);
			UpdateThread.scheduleUpdate();
			return;
		}

		timeSinceCrash ++;

		//motionX *= 0.99;
		//motionY *= 0.99;

		x += motionX;
		y += motionY;

		List<Entity> collidingEntities = new ArrayList<Entity>();

		for(Entity ent : UpdateThread.getSynchronizedEntityList()) {

			if(ent == this) continue;

			float distance = GravityUtil.pow2(ent.x - x) + GravityUtil.pow2(ent.y - y);
			if(distance < GravityUtil.pow2(((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(ent.getId()) + ((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(getId()))) {

				collidingEntities.add(ent);
			}
		}

		if(collidingEntities.size() > 0) {

			for(Entity ent : collidingEntities) {

				if(timeSinceCrash > 10) {

					motionX -= motionX*(((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(ent.getId())/((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(getId()));
					motionY -= motionY*(((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(ent.getId())/((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(getId()));
					timeSinceCrash = 0;

					//float relativeVelocity = GravityUtil.pow2(ent.motionX - motionX) + GravityUtil.pow2(ent.motionY - motionY);

					/*if(Math.abs(relativeVelocity)*ent.mass > 1) {

						if(ent.getMass() <= getMass()) {
							//mass *= 0.99;
							ent.mass /= 2;
							if(ent.mass >= 0.1f) {
								try {
									Entity ent2 = (Entity) ent.clone();
									SolarTennis.tennis.entities.add(ent2);
									UpdateThread.scheduleUpdate();
								} catch (CloneNotSupportedException e) {
									e.printStackTrace();
								}
							} else {
								SolarTennis.tennis.entities.remove(ent);
								UpdateThread.scheduleUpdate();
								return;
							}

							timeSinceCrash = 0;
						}
					} else {

						if(((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(getId()) <= ent.getMass()) {

							ent.mass += ((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(getId());
							SolarTennis.tennis.entities.remove(this);
							UpdateThread.scheduleUpdate();
							return;
						}
					}*/
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

		//int bx = 0;
		//int by = 0;
		//float mot = 0f;
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

			motionX += mx;///((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(getId());
			motionY += my;///((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(getId());
		}
	}

	public void render(SpriteBatch batch) {

		SolarTennis.tennis.shapes.setProjectionMatrix(SolarTennis.tennis.camera.combined);
		SolarTennis.tennis.shapes.begin(ShapeType.Filled);

		//SolarTennis.tennis.shapes.setColor(motionX, motionY, 1.0f, 1.0f);
		SolarTennis.tennis.shapes.setColor(Color.WHITE);
		SolarTennis.tennis.shapes.circle(x, y, ((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(getId()));

		SolarTennis.tennis.shapes.end();
	}
}