package com.lms.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lms.game.LmsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Last Man Standing - Developer Test.";
		config.width = 800;
		config.height = 600;
		config.resizable = false;
		new LwjglApplication(new LmsGame(), config);
	}
}
