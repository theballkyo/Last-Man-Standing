package com.lms.api;

import java.util.HashMap;

public class PlayerServerAPI {
	private float x;
	private float y;
	private String name;
	private String type;
	
	private int hp;
	
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

	public static void update(String name, float x, float y) {
		PlayerServerAPI pl = playerList.get(name);
		pl.x = x;
		pl.y = y;
	}
	
	public static HashMap<String, PlayerServerAPI> getAll() {
		return playerList;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
