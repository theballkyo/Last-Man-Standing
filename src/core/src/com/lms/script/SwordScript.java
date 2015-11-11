package com.lms.script;

import com.badlogic.ashley.core.Entity;
import com.lms.entity.CoreEntity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class SwordScript implements IScript {

	CoreEntity player;
	
	private TransformComponent tf;
	
	public SwordScript(CoreEntity player) {
		this.player = player;
	}
	
	@Override
	public void init(Entity entity) {
		tf = ComponentRetriever.get(entity, TransformComponent.class);
		
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
