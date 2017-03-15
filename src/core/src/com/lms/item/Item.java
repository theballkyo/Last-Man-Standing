package com.lms.item;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lms.entity.BoxEntity;
import com.uwsoft.editor.renderer.SceneLoader;

public abstract class Item {

	public Vector2 pos;

	private int duration;

	private long sTime;
	private Rectangle rect;
	public String type;

	public int id;

	public long entityId = -1;
	public Entity entity;
	private BoxEntity box;

	public Item(int duration, Vector2 pos, int id) {
		this.duration = duration;
		this.pos = pos;
		sTime = System.currentTimeMillis();
		rect = new Rectangle(pos.x, pos.y, 100, 100);
		this.id = id;
	}

	public void createAnimation(SceneLoader sl) {
		box = new BoxEntity("" + System.currentTimeMillis(), sl, pos);
		box.create();
		entity = box.getEntity();
		entityId = box.getEntity().getId();
	}

	public abstract void onPick(String playerName);

	public abstract void onTimeout();

	public void remove() {
		box.removeEntity();
	}

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
