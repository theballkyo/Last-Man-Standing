package com.lms.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.api.PlayerAPI;
import com.lms.entity.CoreEntity;
import com.lms.entity.MainEntity;
import com.lms.game.LmsConfig;
import com.lms.network.NetworkPing;
import com.lms.scene.SceneManage.SceneName;
import com.lms.script.Player;
import com.lms.script.SwordScript;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent.ButtonListener;

public class StartScene extends Scene{

	private CoreEntity myEntity;
	private SpriteBatch batchFix;
	private BitmapFont font;
	private boolean play = false;
	
	public StartScene(SceneLoader sl, Viewport vp, OrthographicCamera cam, SceneManage sm) {
		super(sl, vp, cam, sm);
	}

	public void create() {
		batchFix = new SpriteBatch();
		font = new BitmapFont();
		sl.loadScene("StartScene", vp);

		sl.addComponentsByTagName("button", ButtonComponent.class);

		sl.entityFactory.getEntityByUniqueId(40).getComponent(ButtonComponent.class)
				.addListener(new ButtonListener() {

					@Override
					public void touchUp() {
						// TODO Auto-generated method stub

					}

					@Override
					public void touchDown() {
						// TODO Auto-generated method stub

					}

					@Override
					public void clicked() {
						System.out.println("dd !");
						play = true;
					}
				});

		PlayerAPI.removeAll();
		
		PlayerAPI.add(LmsConfig.playerName, "figther", 100f, 50f);
		myEntity = PlayerAPI.get(LmsConfig.playerName).getCoreEntity();
		myEntity.addScript(new Player(sl.world, 960f));
		myEntity.addScript(new SwordScript());
	}
	
	public void render() {
		if (play) {
			PlayerAPI.remove(LmsConfig.playerName);
			sm.setScene(SceneName.PlayScene);
			return;
		}
		act();
	}
	
	public void resize(int width, int height) {
		sl.rayHandler.useCustomViewport(vp.getScreenX(), vp.getScreenY(), width, height);
		cam = (OrthographicCamera) vp.getCamera();
		Gdx.app.log("Cam", "" + cam.viewportHeight + " " + height + " " + (cam.viewportHeight - height));
		cam.position.y -= Math.abs(cam.viewportHeight - height);
		cam.position.x -= Math.abs(cam.viewportWidth - width);
		cam.viewportWidth = width;
		cam.viewportHeight = height;

		if (cam.position.y < height / 2) {
			cam.position.y = height / 2;
		}
		if (cam.position.x < width / 2) {
			cam.position.x = width / 2;
		}
	}
	
	public void dispose() {

	}
	
	private void act() {
		if (myEntity.getX() + myEntity.getWidth() >= Gdx.graphics.getWidth()) {
			myEntity.setX(Gdx.graphics.getWidth() - myEntity.getWidth());
		}
	}
}
