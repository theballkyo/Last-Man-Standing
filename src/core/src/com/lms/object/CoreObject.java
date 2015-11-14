package com.lms.object;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class CoreObject {

	private static ShapeRenderer shapes;

	static {
		shapes = new ShapeRenderer();
	}

	public static void draw(float delta, Batch batch, float maxWidth) {
		shapes.setProjectionMatrix(batch.getProjectionMatrix());
		Iterator<BulletObject> iter = BulletObject.bullets.iterator();

		while (iter.hasNext()) {
			try {
				BulletObject r = iter.next();
				if (r.r.x < 0 || r.r.x > maxWidth) {
					iter.remove();
				}
				r.r.x += (delta * 1000) * r.side;
				// System.out.println(r.r.x + ":" + r.r.y);
				shapes.begin(ShapeType.Filled);
				shapes.setColor(1, 0, 0, 1);
				shapes.rect(r.r.x, r.r.y, r.r.width, r.r.height);
				shapes.end();
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
				break;
			}
		}
	}

}
