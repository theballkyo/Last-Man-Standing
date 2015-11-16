package com.lms.object;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.lms.api.PlayerData;

public class SwordObject {
	
	private static ArrayList<SwordObject> swords = new ArrayList<>();
	
	private PlayerData p;
	private int width;
	
	private int i;
	private String owner;
	
	public SwordObject(PlayerData p, int width) {
		this.p = p;
		this.width = width;
		
		i = 0;
	}

	public static void add(SwordObject s) {
		swords.add(s);
	}
	
	public static ArrayList<SwordObject> getAll() {
		return swords;
	}

	public final Vector2 getPos() {
		return new Vector2(p.pos.x + (p.getCoreEntity().getWidth() / 2), p.pos.y + (p.getCoreEntity().getHeight() / 2));
	}

	public int getWidth() {
		return width;
	}

	public int getI() {
		if (i > 180)
			return -1;
		if (p.getCoreEntity().getScaleX() < 0)
			return 360 - i;
		return i;
	}

	public void plusI(float delta) {
		i += 400 * delta;
	}
	public String getOwner() {
		return owner;
	}
}
