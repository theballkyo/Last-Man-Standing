package com.lms.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.data.FrameRange;
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

	public Player(World world, float maxWidth) {
		this.world = world;
		this.maxWidth = maxWidth;
	}

	@Override
	public void init(Entity entity) {
		player = entity;
		Gdx.app.log("E name", "" + player.getId());
		transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
		animation = ComponentRetriever.get(entity, SpriteAnimationStateComponent.class);
		dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
		sac = ComponentRetriever.get(entity, SpriteAnimationComponent.class);
		speed = new Vector2(500, 0);

		sac.frameRangeMap.put("stand", new FrameRange("stand", 0, 14));
		sac.frameRangeMap.put("run", new FrameRange("run", 15, 49));

		FrameRange f = new FrameRange();
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
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !isJump) {
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

		if (speed.y == 0) {
			isJump = false;
		}

		if (transformComponent.y < 7f) {
			speed.y = 0;
			transformComponent.y = 7f;
			isJump = false;
		}

		if (transformComponent.x < 0f) {
			transformComponent.x = 0f;
		}
		
		if (transformComponent.x > maxWidth) {
			transformComponent.x = maxWidth;
		}
		if (isWalk) {

			if (!sac.currentAnimation.equals("run")) {
				System.out.println("Run");
				sac.currentAnimation = "run";
				animation.set(sac);

			}

		} else {
			if (!sac.currentAnimation.equals("stand")) {
				System.out.println("Stand");
				sac.currentAnimation = "stand";
				animation.set(sac);
			}
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
		rayFrom.y -= 2.2f;
		world.rayCast(new RayCastCallback() {

			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

				speed.y = 0;

				transformComponent.y = point.y / PhysicsBodyLoader.getScale();

				return 0;
			}
		}, rayFrom, rayTo);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

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
