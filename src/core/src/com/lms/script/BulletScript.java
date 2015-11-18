package com.lms.script;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.game.LmsConfig;
import com.lms.game.LmsGame;
import com.lms.object.BulletObject;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class BulletScript implements IScript {

	Vector2 pos;

	int side;

	TransformComponent tf;
	SceneLoader sl;
	ShapeRenderer shapes;
	MainItemComponent mic;
	Entity entity;

	private boolean isAtk;

	public BulletScript(int side, SceneLoader sl) {
		this.side = side;
		this.sl = sl;
		isAtk = false;
	}

	@Override
	public void init(Entity entity) {
		this.entity = entity;
		shapes = new ShapeRenderer();
		tf = ComponentRetriever.get(entity, TransformComponent.class);
		mic = ComponentRetriever.get(entity, MainItemComponent.class);
	}

	@Override
	public void act(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.C) && !isAtk) {
			Rectangle r = new Rectangle(tf.x, tf.y, 50, 50);
			BulletObject.add(new BulletObject(r, tf.scaleX, mic.itemIdentifier));
			LmsGame.networkManage.sendBullet(LmsConfig.playerName, r, (int) tf.scaleX);
			isAtk = true;
			PlayerAPI.get(entity.getId()).setGun(true);
			new Thread(() -> {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isAtk = false;
				PlayerAPI.get(entity.getId()).setGun(false);
			}).start();
		}
		Iterator<BulletObject> iter = BulletObject.getAll().iterator();
		while (iter.hasNext()) {
			BulletObject b = iter.next();
			if (!b.owner.equals(LmsConfig.playerName)) {
				continue;
			}
			for (Entry<String, PlayerData> p : PlayerAPI.getAll().entrySet()) {
				PlayerData pd = p.getValue();

				if (pd.isGod()) {
					continue;
				}
				if (pd.getName().equals(b.owner)) {
					continue;
				}

				if (b.r.overlaps(pd.getRect())) {
					System.out.println("Script bullets: " + pd.getName() + " is dead.");
					LmsGame.networkManage.sendDead(b.owner, pd.getName());
					PlayerAPI.dead(pd.getName());
					try {
						iter.remove();
					} catch (ConcurrentModificationException e) {
						e.printStackTrace();
						break;
					} catch (IllegalStateException e) {
						e.printStackTrace();
						break;
					}
					break;
				}
			}
		}
	}

	@Override
	public void dispose() {

	}

}
