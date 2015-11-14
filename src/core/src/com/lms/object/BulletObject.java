package com.lms.object;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class BulletObject {
	public static ArrayList<BulletObject> bullets = new ArrayList<BulletObject>();

	public Rectangle r;
	public float side;
	public String owner;
	
	public BulletObject(Rectangle r, float side, String owner) {
		this.r = r;
		this.side = (side < 0) ? -1 : 1;
		this.owner = owner;
	}

	public static void add(BulletObject b) {
		bullets.add(b);
	}
}
