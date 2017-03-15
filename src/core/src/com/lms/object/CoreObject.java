package com.lms.object;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.lms.api.PlayerAPI;
import com.lms.game.LmsConfig;
import com.lms.game.LmsGame;
import com.lms.item.Item;
import com.uwsoft.editor.renderer.SceneLoader;

public class CoreObject {

	private static ShapeRenderer shapes;

	private static ArrayList<Integer> queneBulletRemove = new ArrayList<>();
	static {
		CoreObject.shapes = new ShapeRenderer();
	}

	public static void addBulletIdRemoveQuene(int id) {
		synchronized (queneBulletRemove) {
			queneBulletRemove.add(id);
		}
	}

	private static boolean isIdContainsBulletRemoveQuene(int id) {
		synchronized (queneBulletRemove) {
			return queneBulletRemove.contains(id);
		}
	}

	public static void draw(float delta, Batch batch, SceneLoader sl) {
		CoreObject.shapes.setProjectionMatrix(batch.getProjectionMatrix());
		synchronized (BulletObject.syncArr) {
			Iterator<BulletObject> iter = BulletObject.getAll().iterator();
			while (iter.hasNext()) {
				try {
					BulletObject r = iter.next();
					if (r.isOutOfRange() || isIdContainsBulletRemoveQuene(r.id)) {
						iter.remove();
					}
					r.r.x += (delta * r.speed) * r.side;
					// System.out.println(r.r.x + ":" + r.r.y);

					if (r.texture == null) {
						if (r.side == -1) {
							r.texture = new Texture(Gdx.files.internal("texture/bulletLeft.png"));
						} else {
							r.texture = new Texture(Gdx.files.internal("texture/bulletRight.png"));
						}
					}

					batch.begin();
					batch.draw(r.texture, r.r.x, r.r.y);
					batch.end();
					if (LmsConfig.debug) {
						shapes.begin(ShapeType.Line);
						shapes.setColor(1, 0, 0, 1);
						// shapes.rect(r.x, r.y, r.width, r.height);
						shapes.rect(r.r.x, r.r.y, r.texture.getWidth(), r.texture.getHeight());
						shapes.end();
					}

				} catch (ConcurrentModificationException e) {
					e.printStackTrace();
					break;
				}
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
				 * shapes.begin(ShapeType.Line); shapes.setColor(0, 0, 0, 0);;
				 * shapes.line(so.getPos(), so.getTargetPos()); shapes.end();
				 */
				so.plusI(Gdx.graphics.getDeltaTime());
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
				break;
			}
		}

		Iterator<Item> items = ItemObject.getAll().iterator();
		synchronized (items) {
			while (items.hasNext()) {
				try {
					Item item = items.next();
					if (item.entityId == -1) {
						item.createAnimation(sl);
					}
					if (item.isPick(PlayerAPI.get(LmsConfig.playerName).getRect())) {
						item.onPick(LmsConfig.playerName);
						LmsGame.networkManage.sendPick(item.id);
						ItemObject.remove(item.id);
					}
				} catch (ConcurrentModificationException e) {
					e.printStackTrace();
					break;
				}
			}
		}

		ItemObject.removeInStack();

	}

}
