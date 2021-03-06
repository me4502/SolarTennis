package com.me4502.SolarTennis.entities;

import com.badlogic.gdx.Gdx;
import com.me4502.SolarTennis.SolarTennis;
import com.me4502.SolarTennis.simulation.GravityUtil;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private int entityId;

    public float x;
    public float y;
    private float motionX, motionY;

    public float mass;

    public Entity(int entityId, float x, float y, float mass) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.mass = mass;
        motionX = 0;
        motionY = 0;
    }

    public int getId() {
        return entityId;
    }

    private int timeSinceCrash = 25;

    public void update() {
        if (mass < 0.1f) {
            SolarTennis.tennis.entities.removeValue(this, true);
            return;
        }

        timeSinceCrash++;

        x += motionX;
        y += motionY;

        List<Entity> collidingEntities = new ArrayList<>();

        for (int i = 0; i < SolarTennis.tennis.entities.size; i++) {
            Entity ent = SolarTennis.tennis.entities.items[i];
            if (ent == this || ent == null) continue;

            float distance = GravityUtil.pow2(ent.x - x) + GravityUtil.pow2(ent.y - y);
            if (distance < GravityUtil.pow2(ent.mass + mass)) {
                collidingEntities.add(ent);
            }
        }

        if (collidingEntities.size() > 0) {
            for (Entity ent : collidingEntities) {
                if (timeSinceCrash > 10) {
                    motionX -= motionX * (ent.mass / mass);
                    motionY -= motionY * (ent.mass / mass);
                    timeSinceCrash = 0;
                }
            }
        }

        if (x < -Gdx.graphics.getWidth() / 2) {
            x++;
            motionX = 0;
        }
        if (x > Gdx.graphics.getWidth() / 2) {
            x--;
            motionX = 0;
        }

        if (y < -Gdx.graphics.getHeight() / 2) {
            y++;
            motionY = 0;
        }
        if (y > Gdx.graphics.getHeight() / 2) {
            y--;
            motionY = 0;
        }
    }

    public void updateGravity() {
        float mx = 0f, my = 0f;

        for (int xx = -1; xx < 2; xx++)
            for (int yy = -1; yy < 2; yy++) {

                if (yy == 0 && xx == 0) continue;

                try {
                    float m = GravityUtil.getGravityAtWithout(x + xx, y + yy, this);

                    mx += xx * m;
                    my += yy * m;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        if (mx != 0 || my != 0) {
            motionX += mx;
            motionY += my;
        }
    }

    public void render() {
        SolarTennis.tennis.shapes.circle(x, y, mass);
    }
}