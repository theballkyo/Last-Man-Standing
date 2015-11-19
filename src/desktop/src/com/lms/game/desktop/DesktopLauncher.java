package com.lms.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lms.game.LmsConfig;
import com.lms.game.LmsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		if (arg.length > 0) {
			System.out.println(arg[0]);
			if (arg[0].equals("HACK"))
			LmsConfig.isHack = true;
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = LmsConfig.title;
		config.width = LmsConfig.width;
		config.height = LmsConfig.height;
		config.resizable = false;
		new LwjglApplication(new LmsGame(), config);
	}
}
