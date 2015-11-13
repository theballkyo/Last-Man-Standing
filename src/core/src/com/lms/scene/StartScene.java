package com.lms.scene;

import java.awt.font.FontRenderContext;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

	private String name = "";
	
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
		batchFix.begin();
		if (name.length() < 10){
		if (Gdx.input.isKeyJustPressed(Input.Keys.A)) name += "A";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.B)) name += "B";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.C)) name += "C";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.D)) name += "D";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.E)) name += "E";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.F)) name += "F";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.G)) name += "G";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.H)) name += "H";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.I)) name += "I";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.J)) name += "J";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.K)) name += "K";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.L)) name += "L";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.M)) name += "M";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.N)) name += "N";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.O)) name += "O";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.P)) name += "P";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) name += "Q";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.R)) name += "R";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.S)) name += "S";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.T)) name += "T";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.U)) name += "U";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.V)) name += "V";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.W)) name += "W";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.X)) name += "X";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) name += "Y";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) name += "Z";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) name += "0";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) name += "1";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) name += "2";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) name += "3";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) name += "4";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) name += "5";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) name += "6";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) name += "7";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) name += "8";
		 if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) name += "9";}
		 if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && name.length() != 0) name = name.substring(0, name.length()-1);
		 font.getData().setScale(5f, 5f);
		 font.draw(batchFix, name, 170, 350);
		batchFix.end();
		
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
