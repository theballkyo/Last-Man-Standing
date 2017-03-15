package com.lms.scene;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.api.PlayerAPI;
import com.lms.entity.CoreEntity;
import com.lms.entity.EntityData;
import com.lms.game.LmsConfig;
import com.lms.game.LmsSound;
import com.lms.scene.SceneManage.SceneName;
import com.lms.script.Player;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent.ButtonListener;

public class StartScene extends Scene {

	private CoreEntity myEntity;
	private SpriteBatch batchFix;
	private BitmapFont font;
	private boolean play = false;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;
	private String name = "";

	private boolean isChange = false;
	private String[] entityList;

	private Random random = new Random();


	public StartScene(SceneLoader sl, Viewport vp, OrthographicCamera cam, SceneManage sm) {
		super(sl, vp, cam, sm);
	}

	@Override
	public void create() {
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/test.otf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 120;
		parameter.color = Color.RED;
		font = generator.generateFont(parameter); // font size 12 pixels
		batchFix = new SpriteBatch();
		// font = new BitmapFont();
		sl.loadScene("StartScene", vp);

		sl.addComponentsByTagName("button", ButtonComponent.class);

		sl.entityFactory.getEntityByUniqueId(19).getComponent(ButtonComponent.class).addListener(new ButtonListener() {

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
				// System.out.println("dd !");
				// isChange = true;
				play = true;
			}
		});

		sl.entityFactory.getEntityByUniqueId(445).getComponent(ButtonComponent.class).addListener(new ButtonListener() {

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
				isChange = true;
			}
		});
		entityList = new String[] { "ninja", "swat", "knight", "cyborg" };

		PlayerAPI.removeAll();

		LmsConfig.playerType = entityList[random.nextInt(entityList.length)];
		PlayerAPI.add(LmsConfig.playerName, LmsConfig.playerType, -200f, -150f);

		for (Entity e : sl.getEngine().getEntities()) {
			EntityData.addEntity(e);
		}

		myEntity = PlayerAPI.get(LmsConfig.playerName).getCoreEntity();
		myEntity.addScript(new Player(sl.world, 1950f, false));
		// myEntity.addScript(new SwordScript());
		// myEntity.addScript(new BulletScript(0, sl));
		LmsSound.playLoginSound();
	}

	@Override
	public void render() {

		if (isChange) {
			myEntity.setX(-999);
			myEntity.setY(-999);
			PlayerAPI.removeAll();
			LmsConfig.playerType = entityList[random.nextInt(entityList.length)];
			PlayerAPI.add(LmsConfig.playerName, LmsConfig.playerType, -100f, 50f);
			myEntity = PlayerAPI.get(LmsConfig.playerName).getCoreEntity();
			myEntity.addScript(new Player(sl.world, 1950f, false));
			isChange = false;
		}
		batchFix.begin();
		if (name.length() < 10) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
				name += "A";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
				name += "B";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
				name += "C";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
				name += "D";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
				name += "E";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
				name += "F";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
				name += "G";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
				name += "H";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
				name += "I";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
				name += "J";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
				name += "K";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
				name += "L";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
				name += "M";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
				name += "N";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
				name += "O";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
				name += "P";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
				name += "Q";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
				name += "R";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
				name += "S";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
				name += "T";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
				name += "U";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
				name += "V";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
				name += "W";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
				name += "X";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
				name += "Y";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
				name += "Z";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
				name += "0";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
				name += "1";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
				name += "2";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
				name += "3";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
				name += "4";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
				name += "5";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
				name += "6";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
				name += "7";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
				name += "8";
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
				name += "9";
			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && name.length() != 0) {
			name = name.substring(0, name.length() - 1);
		}
		font.draw(batchFix, name, 270, 590);
		batchFix.end();

		if (play && name.length() > 3 && name.length() < 11) {
			System.out.println("Play !!");
			PlayerAPI.remove(LmsConfig.playerName);
			LmsConfig.playerName = name;

			if (LmsConfig.isHack) {
				LmsConfig.playerName = "GOD_GM";
			}

			sm.setScene(SceneName.PlayScene);
			play = false;
			return;
		}
		act();
		play = false;
	}

	@Override
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

	@Override
	public void dispose() {
		LmsSound.stopLoginSound();
	}

	private void act() {
		
		if (myEntity.getX() < -500) {
			myEntity.setX(-500);
		}
	}
}
