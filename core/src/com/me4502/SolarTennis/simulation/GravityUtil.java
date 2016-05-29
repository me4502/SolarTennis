package com.me4502.SolarTennis.simulation;

import com.me4502.SolarTennis.SolarTennis;
import com.me4502.SolarTennis.UpdateThread;
import com.me4502.SolarTennis.entities.Entity;
import com.me4502.SolarTennis.entities.components.GravityComponent;

import java.util.List;

public class GravityUtil {

	private static final double GRAVITY_CONSTANT = 0.00667384;

	public static float getGravityAt(float x, float y) {

		float calculated = 0f;

		for(Entity ent : UpdateThread.getSynchronizedEntityList()) {

			if(!SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).has(ent.getId()))
				continue;

			calculated += getSourceGravityAt(ent, x, y, 1);
		}

		return calculated;
	}

	public static float getGravityAt(float x, float y, List<Entity> entities) {

		float calculated = 0f;

		for(Entity ent : entities) {

			if(!SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).has(ent.getId()))
				continue;

			calculated += getSourceGravityAt(ent, x, y, 1);
		}

		return calculated;
	}

	public static float getGravityAtWithout(float x, float y, Entity source) {

		float calculated = 0f;

		for(Entity ent : UpdateThread.getSynchronizedEntityList()) {

			if(!SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).has(ent.getId()))
				continue;

			if(ent == source) continue;

			calculated += getSourceGravityAt(ent, x, y, SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).get(source.getId()));
		}

		return calculated;
	}

	public static float getSourceGravityAt(Entity source, float x, float y, float massAtPoint) {

		float distance = pow2(source.x - x) + pow2(source.y - y);
		float c;
		c = (float) (GRAVITY_CONSTANT * (SolarTennis.tennis.componentManager.getComponent(GravityComponent.class).get(source.getId()) * massAtPoint) / sqrt(distance));

		return c;
	}

	public static float sqrt(float x) {
		return Float.intBitsToFloat(532483686 + (Float.floatToRawIntBits(x) >> 1));
	}


	public static float pow2(float a) {
		return a*a;
	}
}