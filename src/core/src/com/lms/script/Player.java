package com.lms.script;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.entity.CoreEntity;
import com.lms.entity.EntityData;
import com.lms.game.LmsConfig;
import com.lms.game.LmsGame;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
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
	private PhysicsBodyComponent pbc;
	private World world;
	private SpriteAnimationStateComponent animation;
	private Vector2 speed;
	private float gravity = -3000f;
	private CoreEntity ce;
	private float jumpSpeed = 1000f;
	private float decreseX;
	private float maxWidth;
	private PlayerData playerData;

	private int jumpCount = 0;
	private boolean isSpacebar;

	private boolean isJump = false;
	private boolean isWalk = false;
	private boolean isPlay;

	private Rectangle playerPosRect;

	public Player(World world, float maxWidth, boolean isPlay) {
		this.world = world;
		this.maxWidth = maxWidth;
		this.isPlay = isPlay;
	}

	@Override
	public void init(Entity entity) {
		player = entity;
//		Gdx.app.log("E name", "" + player.getId());
		transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
		animation = ComponentRetriever.get(entity, SpriteAnimationStateComponent.class);
		dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
		sac = ComponentRetriever.get(entity, SpriteAnimationComponent.class);
		pbc = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
		speed = PlayerAPI.get(player.getId()).speed;
		sac.currentAnimation = "stand";
		animation.set(sac);
		maxWidth -= dimensionsComponent.width;
		ce = PlayerAPI.get(entity.getId()).getCoreEntity();
		playerData = PlayerAPI.get(player.getId());
		playerData.speedJump = 900f;
		speed.x = 1100f;
	}

	public Entity getEntity() {
		return player;
	}

	@Override
	public void act(float delta) {
		jumpSpeed = playerData.speedJump;
		speed.y += gravity * delta;

		// animation.paused = true;
		isWalk = false;
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && jumpCount < 2 && !isSpacebar) {
			speed.y = jumpSpeed;
			isJump = true;
			isSpacebar = true;
			jumpCount++;
		}
		if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			isSpacebar = false;
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
		float a = speed.y * delta;
		while (a != 0) {
			if (a > 0) {
				transformComponent.y += a;
				break;
			}
			;
			if (!rayCast2()) {
				transformComponent.y -= 8;
				a += 8;
			} else {
				break;
			}
		}

		if (!isPlay) {
			if (transformComponent.y < 0) {
				transformComponent.y = 1;
				isJump = false;
				jumpCount = 0;
				speed.y = 0;
			}
		} else {
			if (transformComponent.y + dimensionsComponent.height < 0 && !playerData.isDead()) {
				LmsGame.networkManage.sendDead("-", LmsConfig.playerName);
				playerData.setIsDead(true);
//				PlayerAPI.dead(LmsConfig.playerName);
				speed.y = 0;
				// transformComponent.y = transformComponent.y;
			} else if (transformComponent.y + dimensionsComponent.height > 0) {
			    playerData.setIsDead(false);
            }
		}
		// if (transformComponent.x < 0) {
		// transformComponent.x = 0;
		// }
		if (transformComponent.x > maxWidth) {
			transformComponent.x = maxWidth;
		}
		if (transformComponent.x < -9700) {
			transformComponent.x = -9700;
		}
		CoreEntity ce = PlayerAPI.get(player.getId()).getCoreEntity();
		if (PlayerAPI.get(player.getId()).isSword()) {
			ce.setAnimation("runsword");
		} else if (PlayerAPI.get(player.getId()).isGun()) {
			ce.setAnimation("rungun");
		} else if (isJump) {
			if (!sac.currentAnimation.equals("jump")) {
				ce.setAnimation("jump");
			}
		} else if (isWalk) {
			ce.setAnimation("run");
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

	private boolean rayCast2() {
		if (speed.y > 0) {
			return false;
		}
		Iterator<Entry<Long, EntityData>> entities = EntityData.getAll().entrySet().iterator();
		while (entities.hasNext()) {
			EntityData e = entities.next().getValue();
			if (e.isPhysic() && e.isRayCast(playerData.getRect().setHeight(20))) {
				speed.y = 0;
				isJump = false;
				jumpCount = 0;
				transformComponent.y = e.getUpperY() - 1;
				return true;
			}
		}
		return false;
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
		// rayTo.y -= 25;
		// System.out.println(rayFrom + ":" + rayTo);
		world.rayCast(new RayCastCallback() {

			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

				speed.y = 0;
				isJump = false;
				jumpCount = 0;
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
