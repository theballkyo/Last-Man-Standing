package com.lms.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.SpineDataComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.AnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import javafx.animation.Animation;

public class Player implements IScript{

	private Entity player;
	private TransformComponent transformComponent;
	private DimensionsComponent dimensionsComponent;
	
	private World world;
	private SpriteAnimationStateComponent animation;
	private Vector2 speed;
	private float gravity = -1200f;
	
	private final float jumpSpeed = 600f;
	private float decreseX;
	
	private boolean isJump = false;
	
	public Player(World world) {
		this.world = world;
	}

	@Override
	public void init(Entity entity) {
		player = entity;
		transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
		animation = ComponentRetriever.get(entity, SpriteAnimationStateComponent.class);
		dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
		speed = new Vector2(500, 0);
	}

	@Override
	public void act(float delta) {
		
		
		
		animation.paused = true;
		
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && speed.y == 0){
			speed.y = jumpSpeed;
			isJump = true;
		}
		
		if(isJump)
			decreseX = speed.x * 0.4f;
		else
			decreseX = 0;
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			transformComponent.x-=(speed.x - decreseX)*delta;
			transformComponent.scaleX = -1f;
			animation.paused = false;
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			transformComponent.x+=(speed.x - decreseX)*delta;
			transformComponent.scaleX = 1f;
			animation.paused = false;
		}
		speed.y+=gravity*delta;
		
		transformComponent.y += speed.y*delta;
		
		if(transformComponent.y < 7f){
			isJump = false;
			speed.y = 0;
			transformComponent.y = 7f;
		}
		
		if(transformComponent.x < 0f) {
			transformComponent.x = 0f;
		}
		
		rayCast();
	}
	
	private void rayCast() {

		float rayGap = dimensionsComponent.height/2;
		
		float raySize = -(speed.y)*Gdx.graphics.getDeltaTime();
		
		if(speed.y > 0) return;
		
		Vector2 rayFrom = new Vector2((transformComponent.x+dimensionsComponent.width/2)*PhysicsBodyLoader.getScale(), (transformComponent.y+rayGap)*PhysicsBodyLoader.getScale());
		Vector2 rayTo = new Vector2((transformComponent.x+dimensionsComponent.width/2)*PhysicsBodyLoader.getScale(), (transformComponent.y - raySize)*PhysicsBodyLoader.getScale());
		rayFrom.y -= 2f;
		world.rayCast(new RayCastCallback(){

			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				
				speed.y = 0;
				Gdx.app.log("Ray", ""+point.y  + " " + PhysicsBodyLoader.getScale());
				transformComponent.y = point.y / PhysicsBodyLoader.getScale();
			
				return 0;
			}
		}, rayFrom, rayTo);
	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	public float getx(){
		return transformComponent.x;
	}
	
	public float gety(){
		return transformComponent.y;
	}

}
