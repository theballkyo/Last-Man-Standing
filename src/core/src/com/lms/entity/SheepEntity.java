package com.lms.entity;

import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class SheepEntity extends CoreEntity{
	
	public SpriteAnimationVO vo;
	
	private SpriteAnimationStateComponent animationState;
	private TransformComponent tf;
	
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
		vo.itemName = entityName;
		vo.playMode = 2;
	}
	
	public void create() {
		entity = sl.entityFactory.createEntity(sl.getRoot(), vo);
		
		// Add entity to scene
		add();
		
		animationState = ComponentRetriever.get(getEntity(), SpriteAnimationStateComponent.class);
		tf = ComponentRetriever.get(getEntity(), TransformComponent.class);
		
		setAnimation(true);
	}
	
	public void setX(float x) {
		tf.x = x;
	}
	
	public void setY(float y) {
		tf.y = y;
	}
	
	public float getX() {
		return tf.x;
	}

	public float getY() {
		return tf.y;
	}

	@Override
	public void setAnimation(boolean play) {
		animationState.paused = !play;
	}

	@Override
	public boolean canAnimation() {
		return true;
	}
	
	@Override
	public String getType() {
		return "sheep";
	}
		
}
