package com.me4502.SolarTennis.simulation;

import java.util.List;

import com.me4502.SolarTennis.SolarTennis;
import com.me4502.SolarTennis.UpdateThread;
import com.me4502.SolarTennis.entities.Entity;
import com.me4502.SolarTennis.entities.components.GravityComponent;

public class GravityUtil {

	public static final double GRAVITY_CONSTANT = 0.00667384;

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

			calculated += getSourceGravityAt(ent, x, y, ((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(source.getId()));
		}

		return calculated;
	}

	public static float getSourceGravityAt(Entity source, float x, float y, float massAtPoint) {

		float distance = pow2(source.x - x) + pow2(source.y - y);
		float c = 0f;
		//if(Math.abs(distance) < 0.5)
		//	c = ((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(source.getId());
		//else
		//	c = ((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(source.getId())/sqrt(distance);//(float) Math.max(0, node.power - 0.1*sqrt(distance));
		c = (float) (GRAVITY_CONSTANT * (((GravityComponent) SolarTennis.tennis.componentManager.getComponent(GravityComponent.class)).get(source.getId()) * massAtPoint) / sqrt(distance));

		return c;
	}

	public static float sqrt(float x) {
		return Float.intBitsToFloat(532483686 + (Float.floatToRawIntBits(x) >> 1));
	}


	/*public static double sqrt(final double a) {
		final long x = Double.doubleToLongBits(a) >> 32;
		double y = Double.longBitsToDouble(x + 1072632448 << 31);

		// repeat the following line for more precision
		//y = (y + a / y) * 0.5;
		return y;
	}*/

	public static float pow2(float a) {

		return a*a;
	}
}