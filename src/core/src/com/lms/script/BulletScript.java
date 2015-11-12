package com.lms.script;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lms.object.BulletObject;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class BulletScript implements IScript {

	Vector2 pos;
	
	int side;
	
	TransformComponent tf;
	SceneLoader sl;
	ShapeRenderer shapes;
	ArrayList<BulletObject> bullets;

	public BulletScript(int side, SceneLoader sl) {
		this.side = side;
		this.sl = sl;
	}
	
	@Override
	public void init(Entity entity) {
		shapes = new ShapeRenderer();

		bullets = BulletObject.bullets;
		tf = ComponentRetriever.get(entity, TransformComponent.class);
	}

	@Override
	public void act(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.C)) {
			System.out.println("Ok c");
			bullets.add(new BulletObject(new Rectangle(tf.x, tf.y, 50, 50), tf.scaleX));
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
