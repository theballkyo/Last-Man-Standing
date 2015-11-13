package com.lms.game;

public class LmsConfig {

	public static final String version = "1.0 Dev";
	public static final String title = "Last Man Standing - Developer Test.";
	public static final int width = 800;
	public static final int height = 600;
	public static final boolean debug = true;
	public enum GameType {
		Client, Server
	}

	public static GameType gameType = GameType.Client;

	public static String host = "mc-sv1.enjoyprice.in.th";

	public static int UDPport = 20156;

	public static int TCPport = 20157;

	public static String playerName = "Dev" + System.currentTimeMillis();
}
