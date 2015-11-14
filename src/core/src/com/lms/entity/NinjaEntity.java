package com.lms.entity;

import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.data.FrameRange;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;

public class NinjaEntity extends CoreEntity {

	public SpriteAnimationVO vo;

	public NinjaEntity(String entityName, SceneLoader sl) {
		super(entityName, sl);
		vo = new SpriteAnimationVO();

		init();

	}

	private void init() {
		vo.animationName = "ninja";
		vo.x = 200f;
		vo.y = 500f;
		vo.layerName = entityName;
		vo.itemIdentifier = entityName;
		vo.itemName = entityName;
		vo.playMode = 2;
		vo.scaleY = 1f;
		vo.scaleX = 1f;
	}

	@Override
	public void create() {
		entity = sl.entityFactory.createEntity(sl.getRoot(), vo);

		add();

		setAnimation(true);
		
		sac.frameRangeMap.put("stand", new FrameRange("stand", 0, 9));
		sac.frameRangeMap.put("run", new FrameRange("run", 10, 21));
		sac.currentAnimation = "stand";
		this.animationState.set(sac);
	}

	@Override
	public String getType() {
		return "ninja";
	}

}
