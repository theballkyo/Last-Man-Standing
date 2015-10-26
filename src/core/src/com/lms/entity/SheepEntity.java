package com.lms.entity;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class SheepEntity extends CoreEntity{
	
	public SpriteAnimationVO vo;
	
	private String entityName;
	public SheepEntity(String entityName, SceneLoader sl){
		super(entityName);
		vo = new SpriteAnimationVO();
		this.sl = sl;
		this.entityName = entityName;
		init();
	}
	
	private void init() {
		vo.animationName = "sheep";
		vo.x = 200f;
		vo.y = 500f;
		vo.layerName = entityName;
		vo.itemIdentifier = entityName;
	}
	
	public void pause() {
		ComponentRetriever.get(getEntity(), SpriteAnimationStateComponent.class).paused = true;
	}
	
	public void create(Entity root) {
		setEntity(sl.entityFactory.createEntity(root, vo));
	}
	
	public void add() {
		getScene().engine.addEntity(getEntity());
	}
	
		
}
