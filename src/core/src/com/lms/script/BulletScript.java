package com.lms.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.scripts.IScript;

public class BulletScript implements IScript {

	Vector2 pos;
	
	public BulletScript(Vector2 pos) {
		this.pos = pos;
	}
	
	@Override
	public void init(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
