package com.me4502.SolarTennis;

import com.me4502.SolarTennis.entities.Entity;

public class UpdateThread extends Thread {

	private static int MAX_FPS = 60;

	private static Entity[] entities;

	private static boolean update = true;

	public static void scheduleUpdate() {

		update = true;
	}

	public static Entity[] getSynchronizedEntityList() {

		return entities;
	}

	@Override
	public void run() {

		while(true) {

			long time = System.currentTimeMillis();

			if(update) {
				entities = SolarTennis.tennis.entities.toArray(new Entity[SolarTennis.tennis.entities.size()]);
				update = false;
			}

			long gravityTime = System.currentTimeMillis();

			for(Entity ent : entities) {

				ent.updateGravity();
			}

			long finalTime = System.currentTimeMillis();

			for(Entity ent : entities) {

				ent.update();
			}

			try {
				if(System.currentTimeMillis() - time > 1000/MAX_FPS) {
					System.out.println("Entity update took: " + (System.currentTimeMillis()-time) + "ms (Grav: " + (System.currentTimeMillis()-gravityTime) + " Final: " + (System.currentTimeMillis()-finalTime) + ") with entity count: " + entities.length);
				}

				Thread.sleep(1000 / MAX_FPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}