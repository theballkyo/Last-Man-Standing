package com.lms.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.SpineDataComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.AnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import javafx.animation.Animation;

public class Player implements IScript{

	private Entity player;
	private TransformComponent transformComponent;
	
	private SpriteAnimationStateComponent animation;
	private Vector2 speed;
	private float gravity = -1200f;
	
	private final float jumpSpeed = 400f;
	private float decreseX;
	
	private boolean isJump = false;
	@Override
	public void init(Entity entity) {
		player = entity;
		transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
		animation = ComponentRetriever.get(entity, SpriteAnimationStateComponent.class);
		speed = new Vector2(500, 0);
	}

	@Override
	public void act(float delta) {
		
		
		
		animation.paused = true;
		
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && speed.y == 0){
			speed.y = jumpSpeed;
			isJump = true;
		}
		Gdx.app.log("isJump", ""+isJump);
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
