package com.lms.script;

import java.util.Map.Entry;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
	private final int lenght = 150;

	private Entity entity;

	public SwordScript() {
		isAtk = false;
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
			LmsGame.networkManage.sendSword(PlayerAPI.get(entity.getId()).getName(), lenght);
			SwordObject.add(new SwordObject(PlayerAPI.get(entity.getId()), lenght));
			isAtk = true;
			PlayerAPI.get(entity.getId()).setSword(true);
			new Thread(() -> {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}

				isAtk = false;
				PlayerAPI.get(entity.getId()).setSword(false);
			}).start();
		}

		for (Entry<String, PlayerData> p : PlayerAPI.getAll().entrySet()) {
			PlayerData pd = p.getValue();

			if (pd.isGod()) {
				continue;
			}

			if (pd.getName().equals(LmsConfig.playerName)) {
				continue;
			}
			if (pd == null || SwordObject.getMe() == null) {
				continue;
			}
			if (SwordObject.getMe().isIntersect(pd.getPolygon())) {
				System.out.println("Script bullets: " + pd.getName() + " is dead.");
				LmsGame.networkManage.sendDead(SwordObject.getMe().getOwner(), pd.getName());
				// PlayerAPI.dead(pd.getName());
				break;
			}
		}

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
