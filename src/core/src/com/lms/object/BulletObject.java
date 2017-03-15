package com.lms.object;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lms.api.PlayerAPI;
import com.lms.game.LmsSound;

public class BulletObject {
	private static ArrayList<BulletObject> bullets = new ArrayList<>();

	public int speed = 2800;

	public Rectangle r;
	public float side;
	public String owner;
	public Texture texture;
	public int id;

	private int bulletRange = 1500;

	private Vector2 startPos;

	public static Object syncArr = new Object();

	public BulletObject(Rectangle r, float side, String owner, int id) {
		this.r = r;
		this.side = (side < 0) ? -1 : 1;
		this.owner = owner;
		this.id = id;
		startPos = new Vector2(r.x, r.y);
		PlayerAPI.get(owner).setGun(true);

		new Thread(() -> {
			try {
				Thread.sleep(300);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			PlayerAPI.get(owner).setGun(false);
		}).start();
	}

	public static void add(BulletObject b) {
		LmsSound.playGun();
		BulletObject.bullets.add(b);
	}

	public static ArrayList<BulletObject> getAll() {
		synchronized (bullets) {
			return BulletObject.bullets;
		}

	}

	public Vector2 getStartPos() {
		return startPos;
	}

	public void setStartPos(Vector2 startPos) {
		this.startPos = startPos;
	}

	public boolean isOutOfRange() {
		if (side == 1) {
			if (startPos.x + bulletRange < r.x) {
				return true;
			}
		} else {
			if (startPos.x - bulletRange > r.x) {
				return true;
			}
		}
		return false;
	}
}
