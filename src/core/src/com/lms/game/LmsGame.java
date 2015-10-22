package com.lms.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class LmsGame extends ApplicationAdapter {
	SceneLoader sl;
	private OrthographicCamera cam;
	private Viewport vp;
	private Player player;

	@Override
	public void create () {
		sl = new SceneLoader();
		vp = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sl.loadScene("MainScene", vp);
		
		ItemWrapper root = new ItemWrapper(sl.getRoot());
		player = new Player(sl.world);
		root.getChild("player").addScript(player);
		
	}
	
	public void act() {
        cam = (OrthographicCamera)vp.getCamera();
        // camera.position.x = deer.getCenterX();

       cam.position.x = player.getx();
       //if(Gdx.input.isKeyPressed(Keys.RIGHT)) cam.position.x += 1f;
       cam.position.y = player.gety();
       
       if(cam.position.y < Gdx.graphics.getHeight() / 2) cam.position.y = Gdx.graphics.getHeight() / 2;
       if(cam.position.x < Gdx.graphics.getWidth() / 2) cam.position.x = Gdx.graphics.getWidth() / 2;
    }
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		act();
		sl.getEngine().update(Gdx.graphics.getDeltaTime());
		
	}
	
	public void resize(int width, int height) {
		sl.rayHandler.useCustomViewport(vp.getScreenX(), vp.getScreenY(), 
				width, height);
		cam = (OrthographicCamera)vp.getCamera();
		Gdx.app.log("Cam", ""+cam.viewportHeight+ " "+height + " " +(cam.viewportHeight - height));
		cam.position.y -= Math.abs(cam.viewportHeight - height);
		cam.position.x -= Math.abs(cam.viewportWidth - width);
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		
		if(cam.position.y < height / 2) cam.position.y = height / 2;
		if(cam.position.x < width / 2) cam.position.x = width / 2;
		//Gdx.app.log(width + "", "" + height);
	}
}
