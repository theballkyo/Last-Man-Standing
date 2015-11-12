package com.lms.object;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class BulletObject {
	public static ArrayList<BulletObject> bullets = new ArrayList<BulletObject>();
	
	private static ShapeRenderer shapes;
	
	private Rectangle r;
	private float side;
	
	static {
		shapes = new ShapeRenderer();
	}
	
	public BulletObject(Rectangle r, float side) {
		this.r = r;
		this.side = (side < 0) ? -1 : 1;
	}
	
	public static void draw(float delta, Batch batch, float maxWidth) {
		shapes.setProjectionMatrix(batch.getProjectionMatrix());
		Iterator<BulletObject> iter = bullets.iterator();
		
		while(iter.hasNext()) {
			BulletObject r = iter.next();
			if (r.r.x < 0 || r.r.x > maxWidth) {
				iter.remove();
			}
			r.r.x += (delta * 1000) * r.side;
			System.out.println(r.r.x + ":" + r.r.y);
			shapes.begin(ShapeType.Filled);
			shapes.setColor(1, 0, 0, 1);
			shapes.rect(r.r.x, r.r.y, r.r.width, r.r.height);
			shapes.end();
		}
	}
}
