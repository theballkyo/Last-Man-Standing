package com.lms.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lms.game.LmsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Last Man Standing - Developer Test.";
		config.width = 1024;
		config.height = 876;
		config.resizable = true;
		new LwjglApplication(new LmsGame(), config);
	}
}
