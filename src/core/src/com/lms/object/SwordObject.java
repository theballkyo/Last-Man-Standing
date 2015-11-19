package com.lms.object;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.game.LmsConfig;
import com.lms.game.LmsSound;

public class SwordObject {

	private static ArrayList<SwordObject> swords = new ArrayList<>();

	private PlayerData p;
	private int width;

	private int i;

	public SwordObject(PlayerData p, int width) {
		this.p = p;
		this.width = width;

		i = 0;
		p.setSword(true);
	}

	public synchronized static SwordObject getMe() {
		try {
			for (SwordObject s : swords) {
				if (s.p.getName().equals(LmsConfig.playerName)) {
					return s;
				}
			}
		} catch (ConcurrentModificationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void add(SwordObject s) {
		swords.add(s);
		LmsSound.playSword();
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
			p.setSword(false);
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
		i += 500 * delta;
	}

	public String getOwner() {
		return p.getName();
	}
	
	public Polygon getPoly() {
		Vector2 dot1 = getTargetPos();
		Vector2 dot2 = getPos();
		Polygon polygon = new Polygon(new float [] {
			dot1.x, dot1.y,
			dot2.x, dot2.y,
			dot1.x, dot1.y
		});
		return polygon;
	}
	
	public boolean isIntersect(Polygon poly) {
		return Intersector.intersectSegmentPolygon(getPos(), getTargetPos(), poly);
	}
}
