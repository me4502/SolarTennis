package com.me4502.SolarTennis;

import com.me4502.SolarTennis.entities.Entity;

import java.util.Locale;

public class UpdateThread extends Thread {

    private static final int SIMULATION_FPS = 60;
    private static final int REQUIRED_FPS = 1000 / SIMULATION_FPS;

    private static final String OVERLOAD_WARNING_STRING = "Update took: %d ms. (%d Update: %d) Entity Count: %d";

    private static boolean isRunning = true;

    @Override
    public void run() {
        while (isRunning) {
            long time = System.currentTimeMillis();

            long gravityTime = System.currentTimeMillis();

            for (int i = 0; i < SolarTennis.tennis.entities.size; i++) {
                Entity ent = SolarTennis.tennis.entities.items[i];
                if (ent == null) continue;
                ent.updateGravity();
            }

            long updateTime = System.currentTimeMillis();

            for (int i = 0; i < SolarTennis.tennis.entities.size; i++) {
                Entity ent = SolarTennis.tennis.entities.items[i];
                if (ent == null) continue;
                ent.update();
            }

            try {
                if (System.currentTimeMillis() - time > REQUIRED_FPS) {
                    System.out.println(String.format(Locale.ENGLISH, OVERLOAD_WARNING_STRING,
                            System.currentTimeMillis() - time, updateTime - gravityTime,
                            System.currentTimeMillis() - updateTime, SolarTennis.tennis.entities.size));
                }

                Thread.sleep(REQUIRED_FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static void disable() {
        isRunning = false;
    }
}