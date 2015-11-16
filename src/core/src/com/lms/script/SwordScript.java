package com.lms.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.lms.api.PlayerAPI;
import com.lms.entity.CoreEntity;
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
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			System.out.println(entity.getId());
			SwordObject.add(new SwordObject(PlayerAPI.get(entity.getId()), 100));
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
