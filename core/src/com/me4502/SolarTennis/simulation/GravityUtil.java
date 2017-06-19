package com.me4502.SolarTennis.simulation;

import com.me4502.SolarTennis.SolarTennis;
import com.me4502.SolarTennis.entities.Entity;

public class GravityUtil {

    private static final float GRAVITY_CONSTANT = 0.00667384f;

    public static float getGravityAt(float x, float y) {
        float calculated = 0f;

        for (int i = 0; i < SolarTennis.tennis.entities.size; i++) {
            Entity ent = SolarTennis.tennis.entities.items[i];
            if (ent == null) continue;

            calculated += getSourceGravityAt(ent, x, y, 1);
        }

        return calculated;
    }

    public static float getGravityAtWithout(float x, float y, Entity source) {
        float calculated = 0f;

        for (int i = 0; i < SolarTennis.tennis.entities.size; i++) {
            Entity ent = SolarTennis.tennis.entities.items[i];
            if (ent == null) continue;
            if (ent == source) continue;

            calculated += getSourceGravityAt(ent, x, y, source.mass);
        }

        return calculated;
    }

    public static float getSourceGravityAt(Entity source, float x, float y, float massAtPoint) {
        float distance = pow2(source.x - x) + pow2(source.y - y);

        return GRAVITY_CONSTANT * (source.mass * massAtPoint) / sqrt(distance);
    }

    public static float sqrt(float x) {
        return Float.intBitsToFloat(532483686 + (Float.floatToRawIntBits(x) >> 1));
    }


    public static float pow2(float a) {
        return a * a;
    }
}