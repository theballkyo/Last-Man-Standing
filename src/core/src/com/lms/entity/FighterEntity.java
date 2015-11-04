package com.lms.entity;

import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class FighterEntity extends CoreEntity {

public SpriteAnimationVO vo;
	
	private SpriteAnimationStateComponent animationState;
	private TransformComponent tf;
	
	public FighterEntity(String entityName, SceneLoader sl) {
		super(entityName, sl);
		vo = new SpriteAnimationVO();
		
		init();
	}

	private void init() {
		vo.animationName = "player";
		vo.x = 200f;
		vo.y = 500f;
		vo.layerName = entityName;
		vo.itemIdentifier = entityName;
		vo.itemName = entityName;
		vo.playMode = 2;
		vo.scaleY = 1f;
	}
	
	@Override
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

	public float getScaleX() {
		return tf.scaleX;
	}
	
	public void setScaleX(float x) {
		tf.scaleX = x;
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

	@Override
	public void setScaleY(float y) {
		tf.scaleY = y;
	}

	@Override
	public float getScaleY() {
		return tf.scaleY;
	}


}
