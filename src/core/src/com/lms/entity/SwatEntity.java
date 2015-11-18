package com.lms.entity;

import com.badlogic.gdx.math.Vector2;
import com.lms.api.PlayerAPI;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.data.FrameRange;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;

public class SwatEntity extends CoreEntity{
	public SpriteAnimationVO vo;

	public SwatEntity(String entityName, SceneLoader sl) {
		super(entityName, sl);
		vo = new SpriteAnimationVO();

		init();

		setScale(new Vector2(0.4f, 0.7f));
	}

	private void init() {
		vo.animationName = "swat";
		vo.x = PlayerAPI.get(entityName).pos.x;
		vo.y = PlayerAPI.get(entityName).pos.y;
		vo.layerName = entityName;
		vo.itemIdentifier = entityName;
		vo.itemName = entityName;
		vo.playMode = 2;
		vo.scaleY = 1f;
		vo.scaleX = 1f;

		speed.x = 700;
	}

	@Override
	public void create() {
		entity = sl.entityFactory.createEntity(sl.getRoot(), vo);

		add();

		setAnimation(true);

		sac.frameRangeMap.put("stand", new FrameRange("stand", 0, 9));
		sac.frameRangeMap.put("run", new FrameRange("run", 10, 21));
		sac.frameRangeMap.put("gun", new FrameRange("stand", 22, 35));
		sac.frameRangeMap.put("sword", new FrameRange("run", 36, 50));
		sac.currentAnimation = "stand";
		animationState.set(sac);
	}

	@Override
	public String getType() {
		return "swat";
	}

}
