package com.lms.object;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.lms.api.PlayerAPI;
import com.lms.game.LmsSound;

public class BulletObject {
	private static ArrayList<BulletObject> bullets = new ArrayList<BulletObject>();

	public Rectangle r;
	public float side;
	public String owner;

	public BulletObject(Rectangle r, float side, String owner) {
		this.r = r;
		this.side = (side < 0) ? -1 : 1;
		this.owner = owner;
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
		bullets.add(b);
	}

	public static ArrayList<BulletObject> getAll() {
		return bullets;
	}

}
