package com.lms.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.lms.api.PlayerAPI;
import com.lms.entity.CoreEntity;
import com.lms.game.LmsConfig;
import com.lms.game.LmsGame;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class Player implements IScript {

	private Entity player;
	private TransformComponent transformComponent;
	private DimensionsComponent dimensionsComponent;
	private SpriteAnimationComponent sac;
	private World world;
	private SpriteAnimationStateComponent animation;
	private Vector2 speed;
	private float gravity = -1200f;

	private final float jumpSpeed = 600f;
	private float decreseX;
	private float maxWidth;

	private boolean isJump = false;
	private boolean isWalk = false;
	private boolean isPlay;

	public Player(World world, float maxWidth, boolean isPlay) {
		this.world = world;
		this.maxWidth = maxWidth;
		this.isPlay = isPlay;
	}

	@Override
	public void init(Entity entity) {
		player = entity;
		Gdx.app.log("E name", "" + player.getId());
		transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
		animation = ComponentRetriever.get(entity, SpriteAnimationStateComponent.class);
		dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
		sac = ComponentRetriever.get(entity, SpriteAnimationComponent.class);
		speed = PlayerAPI.get(player.getId()).speed;
		sac.currentAnimation = "stand";
		animation.set(sac);
		maxWidth -= dimensionsComponent.width;
	}

	public Entity getEntity() {
		return player;
	}

	@Override
	public void act(float delta) {
		speed.y += gravity * delta;
		rayCast();

		// animation.paused = true;
		isWalk = false;
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !isJump && speed.y >= 0) {
			speed.y = jumpSpeed;
			isJump = true;
		}

		if (isJump) {
			decreseX = speed.x * 0.4f;
		} else {
			decreseX = 0;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			transformComponent.x -= (speed.x - decreseX) * delta;
			transformComponent.scaleX = -Math.abs(transformComponent.scaleX);
			isWalk = true;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			transformComponent.x += (speed.x - decreseX) * delta;
			transformComponent.scaleX = Math.abs(transformComponent.scaleX);
			isWalk = true;
		}

		transformComponent.y += speed.y * delta;
		if (!isPlay) {
			if (transformComponent.y < 0) {
				transformComponent.y = 1;
				isJump = false;
				speed.y = 0;
			}
		} else {
			if (transformComponent.y + dimensionsComponent.height < 0) {
				LmsGame.networkManage.sendDead("-", LmsConfig.playerName);
				PlayerAPI.dead(LmsConfig.playerName);
			}
		}
		//if (transformComponent.x < 0) {
		//	transformComponent.x = 0;
		//}
		if (transformComponent.x > maxWidth) {
			transformComponent.x = maxWidth;
		}
		CoreEntity ce = PlayerAPI.get(player.getId()).getCoreEntity();
		if (isWalk) {
			if (!sac.currentAnimation.equals("run")) {
				ce.setAnimation("run");

			}
		} else if (PlayerAPI.get(player.getId()).isSword()) {
			if (!sac.currentAnimation.equals("sword")) {
				ce.setAnimation("sword");
			}
		} else if (PlayerAPI.get(player.getId()).isGun()) {
			if (!sac.currentAnimation.equals("gun")) {
				ce.setAnimation("gun");
			}
		} else {
			if (!sac.currentAnimation.equals("stand")) {
				ce.setAnimation("stand");
			}
		} 
		// animation.set(sac);

	}

	private void rayCast2() {
		if (speed.y > 0) {
			return;
		}
	}

	private void rayCast() {

		float rayGap = dimensionsComponent.height / 2;

		float raySize = -(speed.y) * Gdx.graphics.getDeltaTime();

		if (speed.y > 0) {
			return;
		}

		Vector2 rayFrom = new Vector2(
				(transformComponent.x + dimensionsComponent.width / 2) * PhysicsBodyLoader.getScale(),
				(transformComponent.y + rayGap) * PhysicsBodyLoader.getScale());
		Vector2 rayTo = new Vector2(
				(transformComponent.x + dimensionsComponent.width / 2) * PhysicsBodyLoader.getScale(),
				(transformComponent.y - raySize) * PhysicsBodyLoader.getScale());
		rayFrom.y -= 2f;
		//rayTo.y -= 25;
		world.rayCast(new RayCastCallback() {

			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

				speed.y = 0;
				isJump = false;
				transformComponent.y = point.y / PhysicsBodyLoader.getScale();
				// System.out.println();
				return 0;
			}
		}, rayFrom, rayTo);
	}

	@Override
	public void dispose() {

	}

	public float getx() {
		return transformComponent.x;
	}

	public float gety() {
		return transformComponent.y;
	}

	public float getScaleX() {
		return transformComponent.scaleX;
	}
}
