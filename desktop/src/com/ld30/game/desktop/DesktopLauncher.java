package com.ld30.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ld30.game.LD30;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 660;
		config.height = 660; //TODO REMOVE
		new LwjglApplication(new LD30(), config);
	}
}
