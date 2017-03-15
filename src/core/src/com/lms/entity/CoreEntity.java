package com.lms.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.ScriptComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
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
	protected ZIndexComponent zindexc;
	public TintComponent tc;

	protected String entityName;

	protected Vector2 position;

	public Vector2 speed;

	abstract public void create();

	abstract public String getType();

	public String scene = "";

	protected Vector2 scale;

	public CoreEntity(String entityName, SceneLoader sl) {
		this.entityName = entityName;
		this.sl = sl;
		speed = new Vector2();
		scale = new Vector2(1, 1);
	}

	protected void add() {
		scene = sl.getSceneVO().sceneName;

		sl.engine.addEntity(entity);

		animationState = ComponentRetriever.get(entity, SpriteAnimationStateComponent.class);
		sac = ComponentRetriever.get(entity, SpriteAnimationComponent.class);
		tf = ComponentRetriever.get(entity, TransformComponent.class);
		dc = ComponentRetriever.get(entity, DimensionsComponent.class);
		tc = ComponentRetriever.get(entity, TintComponent.class);
		zindexc = ComponentRetriever.get(entity, ZIndexComponent.class);

		zindexc.setZIndex(5000);
		// zindexc.layerName = "player";
		tf.scaleX = 1f;
		tf.scaleY = 1f;
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

	public void removeEntity() {
		sl.engine.removeEntity(entity);
	}

	public boolean isJump() {
		return sac.currentAnimation.equals("jump");
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

	public void setAnimation(String name) {
		sac.playMode = PlayMode.LOOP;
		if (name.equals("run")) {
			if (sac.currentAnimation.equals("run")) {
				return;
			}
			sac.currentAnimation = "run";
		} else if (name.equals("stand")) {
			if (sac.currentAnimation.equals("stand")) {
				return;
			}
			sac.currentAnimation = "stand";
		} else if (name.equals("sword")) {
			if (sac.currentAnimation.equals("sword")) {
				return;
			}
			sac.currentAnimation = "sword";
		} else if (name.equals("gun")) {
			if (sac.currentAnimation.equals("gun")) {
				return;
			}
			sac.currentAnimation = "gun";
		} else if (name.equals("runsword")) {
			if (sac.currentAnimation.equals("runsword")) {
				return;
			}
			sac.currentAnimation = "runsword";
		} else if (name.equals("rungun")) {
			if (sac.currentAnimation.equals("rungun")) {
				return;
			}
			sac.currentAnimation = "rungun";
		} else if (name.equals("jump")) {
			if (sac.currentAnimation.equals("jump")) {
				return;
			}
			sac.currentAnimation = "jump";
			sac.playMode = PlayMode.NORMAL;
		} else if (name.equals("fall")) {
			if (sac.currentAnimation.equals("fall")) {
				return;
			}
			sac.currentAnimation = "fall";
		}

		animationState.set(sac);
	}

	public Vector2 getScale() {
		return scale;
	}

	public void setScale(Vector2 scale) {
		this.scale = scale;
	}
}
