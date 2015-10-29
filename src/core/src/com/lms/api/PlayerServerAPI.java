package com.lms.api;

import java.util.HashMap;

public class PlayerServerAPI {
	private float x;
	private float y;
	private String name;
	private String type;
	
	private int hp;
	public long lastConn;
	
	private static HashMap<String, PlayerServerAPI> playerList = new HashMap<String, PlayerServerAPI>();
	
	public PlayerServerAPI(String name, String type, float x, float y) {
		this.name = name;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public static void add(String name, String type, float x, float y) {
		playerList.put(name, new PlayerServerAPI(name, type , x, y));
	}

	public static void remove(String name) {
		playerList.remove(name);
	}
	
	public static void update(String name, float x, float y) {
		PlayerServerAPI pl = playerList.get(name);
		pl.x = x;
		pl.y = y;
	}
	
	public static void setLastConn(String name, long time) {
		PlayerServerAPI pl = playerList.get(name);
		pl.lastConn = time;
	}
	
	public static long getLastConn(String name) {
		return playerList.get(name).lastConn;
	}
	
	public static HashMap<String, PlayerServerAPI> getAll() {
		return playerList;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public String getType() {
		return type;
	}
}
