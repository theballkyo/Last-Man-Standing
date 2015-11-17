package com.lms.item;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Item {

	public Vector2 pos;

	private int duration;

	private long sTime;
	private Rectangle rect;
	public String type;

	public Item(int duration, Vector2 pos) {
		this.duration = duration;
		this.pos = pos;
		sTime = System.currentTimeMillis();
		rect = new Rectangle(pos.x, pos.y, 100, 100);
	}

	public abstract void onPick(String playerName);

	public abstract void onTimeout();

	public long timeRemain() {
		return System.currentTimeMillis() + duration - sTime;
	}

	public boolean isTimeout() {
		return sTime + duration < System.currentTimeMillis();
	}

	public boolean isPick(Vector2 pos) {
		return rect.contains(pos);
	}

	public boolean isPick(Rectangle r) {
		return rect.overlaps(r);
	}

	public Rectangle getRect() {
		return rect;
	}
}
