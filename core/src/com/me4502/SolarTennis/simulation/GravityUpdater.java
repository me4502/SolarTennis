package com.me4502.SolarTennis.simulation;



public class GravityUpdater extends Thread {

	/*public static float[][] gravityGrid;

	public GravityUpdater(int width, int height) {

		gravityGrid = new float[width][height];
	}

	public void scheduleUpdate(GravityNode ... nodes) {

		synchronized(gravityGrid) {

			nodeQueue.offer(nodes);
		}
	}

	BlockingQueue<GravityNode[]> nodeQueue = new ArrayBlockingQueue<GravityNode[]>(2);

	public static double sqrt(final double a) {
		final long x = Double.doubleToLongBits(a) >> 32;
		double y = Double.longBitsToDouble(x + 1072632448 << 31);

		// repeat the following line for more precision
		y = (y + a / y) * 0.5;
		return y;
	}

	public static double pow2(double a) {

		return a*a;
	}

	float lerp(float v0, float v1, float t) {
		return (1-t)*v0 + t*v1;
	}

	@Override
	public void run() {

		while(true) {

			GravityNode[] nodes = nodeQueue.poll();

			if(nodes != null) {
				synchronized(gravityGrid) {
					for(int x = 0; x < gravityGrid.length; x++) {
						for(int y = 0; y < gravityGrid[x].length; y++) {

							float calculated = 0f;

							for(GravityNode node : nodes) {

								//Calculate the distance from the source of gravity.
								float distance = (float) ((float) pow2(node.x - x) + pow2(node.y - y));
								float c = 0f;
								if(Math.abs(distance) < 0.5)
									c = node.power;
								else
									c = (float) (node.power/sqrt(distance));//(float) Math.max(0, node.power - 0.1*sqrt(distance));

								//if(c > calculated)
								calculated += c;
							}

							gravityGrid[x][y] = calculated;//Math.min(power, (float) Math.max(0, pow2(power) / closest));//Math.min(1, power/closest);
						}
					}
				}
			}
		}
	}*/
}