package com.lms.item;

import com.badlogic.gdx.math.Vector2;
import com.lms.api.PlayerData;

public abstract class Item {

	public Vector2 pos;

	private long expire;
	private int timeout;

	public String type;

	public Item(long expire, int timeout) {
		this.expire = expire;
		this.timeout = timeout;
	}

	public abstract void onPick(PlayerData pd);

	public abstract void onTimeout();

}
