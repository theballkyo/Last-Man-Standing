package com.lms.object;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.lms.api.PlayerAPI;
import com.lms.game.LmsConfig;
import com.lms.item.Item;

public class CoreObject {

	private static ShapeRenderer shapes;
	

	static {
		shapes = new ShapeRenderer();
	}

	public static void draw(float delta, Batch batch, float maxWidth) {
		shapes.setProjectionMatrix(batch.getProjectionMatrix());

		Iterator<BulletObject> iter = BulletObject.getAll().iterator();
		while (iter.hasNext()) {
			try {
				BulletObject r = iter.next();
				if (r.r.x < -1920 || r.r.x > maxWidth) {
					iter.remove();
				}
				r.r.x += (delta * 1000) * r.side;
				// System.out.println(r.r.x + ":" + r.r.y);
				Texture bulletLeft = new Texture(Gdx.files.internal("texture/bulletLeft.png"));
				Texture bulletRight = new Texture(Gdx.files.internal("texture/bulletRight.png"));
				batch.begin();
				if (r.side == -1)
					batch.draw(bulletLeft, r.r.x, r.r.y);
				else if (r.side == 1)
					batch.draw(bulletRight, r.r.x, r.r.y);
				batch.end();
				
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
				break;
			}
		}

		Iterator<SwordObject> sw = SwordObject.getAll().iterator();
		while (sw.hasNext()) {
			try {
				SwordObject so = sw.next();
				if (so.getI() == -1) {
					sw.remove();
					continue;
				}
				/*
				shapes.begin(ShapeType.Line);
				shapes.setColor(0, 0, 0, 0);;
				shapes.line(so.getPos(), so.getTargetPos());
				shapes.end();
				*/
				so.plusI(Gdx.graphics.getDeltaTime());
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
				break;
			}
		}

		Iterator<Item> items = ItemObject.getAll().iterator();
		while (items.hasNext()) {
			try {
				Item item = items.next();
				shapes.begin(ShapeType.Line);
				shapes.setColor(1, 0, 0, 1);
				shapes.rect(item.getRect().x, item.getRect().y, item.getRect().width, item.getRect().height);
				shapes.end();
				if (item.isPick(PlayerAPI.get(LmsConfig.playerName).getRect())) {
					item.onPick(LmsConfig.playerName);
					items.remove();
				}
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
				break;
			}
		}
	}

}
