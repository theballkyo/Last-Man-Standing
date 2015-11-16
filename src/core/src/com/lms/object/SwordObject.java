package com.lms.object;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lms.api.PlayerData;
import com.lms.game.LmsConfig;

public class SwordObject {

	private static ArrayList<SwordObject> swords = new ArrayList<>();

	private PlayerData p;
	private int width;

	private int i;

	public SwordObject(PlayerData p, int width) {
		this.p = p;
		this.width = width;

		i = 0;
	}

	public static SwordObject getMe() {
		for (SwordObject s: swords) {
			if (s.p.getName().equals(LmsConfig.playerName))
				return s;
		}
		return null;
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
		if (i > 180) {
			return -1;
		}
		if (p.getCoreEntity().getScaleX() < 0) {
			return 360 - i;
		}
		return i;
	}

	public Vector2 getTargetPos() {
		return new Vector2(getPos().x + (getWidth() * MathUtils.sinDeg(getI())),
				getPos().y + (getWidth() * MathUtils.cosDeg(getI())));
	}
	public void plusI(float delta) {
		i += 400 * delta;
	}

	public String getOwner() {
		return p.getName();
	}
}
