package com.lms.object;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CoreObject {

	private static ShapeRenderer shapes;

	static Vector2 v1 = new Vector2(200, 200);
	static Vector2 v2 = new Vector2(5, 5);
	
	static {
		shapes = new ShapeRenderer();
		v2 = new Vector2(v1.x + (5 * MathUtils.sinDeg(5)), v1.y + (5 * MathUtils.cosDeg(5)));
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
		
		Iterator<SwordObject> sw = SwordObject.getAll().iterator();
		
		while(sw.hasNext()) {
			SwordObject so = sw.next();
			if (so.getI() == -1) {
				sw.remove();
				continue;
			}
			Vector2 targetPos = new Vector2(so.getPos().x + (so.getWidth() * MathUtils.sinDeg(so.getI())),
					so.getPos().y + (so.getWidth() * MathUtils.cosDeg(so.getI())));

			shapes.begin(ShapeType.Filled);
			shapes.setColor(1, 0, 0, 1);
			shapes.line(so.getPos(), targetPos);
			shapes.end();
			so.plusI(Gdx.graphics.getDeltaTime());
		}
	}

}
