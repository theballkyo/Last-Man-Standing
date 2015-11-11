package com.lms.entity;

import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;

public class FighterEntity extends CoreEntity {

	public SpriteAnimationVO vo;

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

		setAnimation(true);
	}

	@Override
	public String getType() {
		return "figther";
	}

}
