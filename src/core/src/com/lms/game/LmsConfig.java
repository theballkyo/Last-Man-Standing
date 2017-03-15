package com.lms.game;

import com.uwsoft.editor.renderer.SceneLoader;

public class LmsConfig {

	public static final String version = "0.9.60 Beta";
	public static final String title = "Last Man Standing";
	public static final int width = 1280;
	public static final int height = 800;
	public static boolean debug = false;
	public static String playerType;

	public enum GameType {
		Client, Server
	}

	public static GameType gameType = GameType.Client;

	public static String host = "127.0.0.1";

	public static int UDPport = 20156;

	public static int TCPport = 20157;

	public static String playerName = "Dev" + System.currentTimeMillis();

	public static int errorCode = 0;

	public static boolean isHack = false;

	public static SceneLoader sl;
}
