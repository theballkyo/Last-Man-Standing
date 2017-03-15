package com.lms.entity;

import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;

public class BoxEntity extends CoreEntity {

	public SpriteAnimationVO vo;

	public Vector2 pos;

	public BoxEntity(String entityName, SceneLoader sl, Vector2 pos) {

		super(entityName, sl);
		vo = new SpriteAnimationVO();
		this.pos = pos;
		init();

		setScale(new Vector2(0.4f, 0.7f));
	}

	private void init() {
		vo.animationName = "box";
		vo.x = pos.x;
		vo.y = pos.y;
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

	}

	@Override
	public String getType() {
		return "box";
	}

}
