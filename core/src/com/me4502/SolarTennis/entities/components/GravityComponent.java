package com.me4502.SolarTennis.entities.components;

import java.util.Arrays;

public class GravityComponent extends Component {

	float[] gravity = new float[16];

	public float get(int entityId) {

		if(entityId < gravity.length)
			return gravity[entityId];
		return 0.0f;
	}

	public void set(int entityId, float value) {

		while(gravity.length <= entityId) {
			gravity = Arrays.copyOf(gravity, gravity.length + 16);
		}

		gravity[entityId] = value;
	}

	@Override
	public boolean has(int entityId) {

		return get(entityId) != 0.0f;
	}
}