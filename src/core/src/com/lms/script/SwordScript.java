package com.lms.script;

import java.util.ConcurrentModificationException;
import java.util.Map.Entry;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.entity.CoreEntity;
import com.lms.game.LmsConfig;
import com.lms.game.LmsGame;
import com.lms.object.SwordObject;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class SwordScript implements IScript {

	CoreEntity player;

	private TransformComponent tf;
	private boolean isAtk;
	private int i;

	private Entity entity;

	public SwordScript() {
		isAtk = false;
		i = 0;
	}

	@Override
	public void init(Entity entity) {
		tf = ComponentRetriever.get(entity, TransformComponent.class);
		this.entity = entity;
	}

	@Override
	public void act(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.X) && !isAtk) {
			System.out.println(entity.getId());
			LmsGame.networkManage.sendSword(PlayerAPI.get(entity.getId()).getName(), 100);
			SwordObject.add(new SwordObject(PlayerAPI.get(entity.getId()), 100));
			isAtk = true;
			
			new Thread(() -> {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				isAtk = false;
			}).start();
		}
		
		if (isAtk) {
			for (Entry<String, PlayerData> p : PlayerAPI.getAll().entrySet()) {
				PlayerData pd = p.getValue();

				if (pd.isGod()) {
					continue;
				}
				
				if (pd.getName().equals(LmsConfig.playerName))
					continue;
				
				if (new Rectangle(pd.pos.x, pd.pos.y, pd.getCoreEntity().getWidth(), pd.getCoreEntity().getHeight()).contains(SwordObject.getMe().getTargetPos())) {
					System.out.println("Script bullets: " + pd.getName() + " is dead.");
					LmsGame.networkManage.sendDead(SwordObject.getMe().getOwner(), pd.getName());
					PlayerAPI.dead(pd.getName());
					break;
				}
			}
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
