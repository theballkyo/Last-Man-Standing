package com.lms.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.ScriptComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public abstract class CoreEntity {

	protected Entity entity;
	protected SceneLoader sl;

	protected SpriteAnimationStateComponent animationState;
	protected SpriteAnimationComponent sac;
	protected TransformComponent tf;
	protected DimensionsComponent dc;
	public TintComponent tc;

	protected String entityName;

	protected Vector2 position;

	public Vector2 speed;

	abstract public void create();

	abstract public String getType();

	public String scene = "";

	public CoreEntity(String entityName, SceneLoader sl) {
		this.entityName = entityName;
		this.sl = sl;
		speed = new Vector2();
	}

	protected void add() {
		scene = sl.getSceneVO().sceneName;
		sl.engine.addEntity(entity);

		animationState = ComponentRetriever.get(entity, SpriteAnimationStateComponent.class);
		sac = ComponentRetriever.get(entity, SpriteAnimationComponent.class);
		tf = ComponentRetriever.get(entity, TransformComponent.class);
		dc = ComponentRetriever.get(entity, DimensionsComponent.class);
		tc = ComponentRetriever.get(entity, TintComponent.class);
	}

	public void addScript(IScript script) {
		new ItemWrapper(sl.getRoot()).addChild(entity).addScript(script);
	}

	public void removeScript() {
		entity.remove(ScriptComponent.class);
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

	public float getWidth() {
		return dc.width;
	}

	public float getHeight() {
		return dc.height;
	}

	public void setScaleX(float x) {
		tf.scaleX = x;
	}

	public void setAnimation(boolean play) {
		animationState.paused = !play;
	}

	public boolean canAnimation() {
		return true;
	}

	public void setScaleY(float y) {
		tf.scaleY = y;
	}

	public float getScaleY() {
		return tf.scaleY;
	}

	public SceneLoader getScene() {
		return sl;
	}

	public Entity getEntity() {
		return entity;
	}

	public String getName() {
		return entityName;
	}

	public Vector2 getPosition() {
		return position;
	}

	public boolean isWalk() {
		if (sac == null) {
			return false;
		}
		return sac.currentAnimation.equals("run");
	}

	public void setWalk(boolean r) {
		if (r) {
			if (sac.currentAnimation.equals("run")) {
				return;
			}
			sac.currentAnimation = "run";
		} else {
			if (sac.currentAnimation.equals("stand")) {
				return;
			}
			sac.currentAnimation = "stand";
		}

		animationState.set(sac);
	}
}
