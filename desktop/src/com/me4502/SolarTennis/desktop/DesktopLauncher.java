package com.me4502.SolarTennis.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me4502.SolarTennis.SolarTennis;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.samples = 16;

		new LwjglApplication(new SolarTennis(), config);
	}
}
