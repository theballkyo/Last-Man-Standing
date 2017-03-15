package com.lms.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lms.game.LmsConfig;
import com.lms.game.LmsGame;

public class DesktopLauncher {
	public static void main (String[] args) {
		if (args.length > 0) {
			for (String arg: args)
			if (arg.equals("HACK")) {
				LmsConfig.isHack = true;
			} else if (arg.startsWith("--ip=")) {
				String ip = arg.substring(5);
				System.out.println(ip);
				LmsConfig.host = ip;
			} else if (arg.equalsIgnoreCase("--debug")) {
				LmsConfig.debug = true;
			}
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = LmsConfig.title;
		config.width = LmsConfig.width;
		config.height = LmsConfig.height;
		config.resizable = false;
		new LwjglApplication(new LmsGame(), config);
	}
}
