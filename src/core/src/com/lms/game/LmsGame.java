package com.lms.game;

import java.util.Map.Entry;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.entity.CoreEntity;
import com.lms.entity.MainEntity;
import com.lms.network.NetworkEventJoin;
import com.lms.network.NetworkManage;
import com.lms.scene.SceneManage;
import com.lms.scene.SceneManage.SceneName;
import com.lms.script.Player;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent.ButtonListener;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class LmsGame extends ApplicationAdapter {

	private SceneLoader sl;
	private SpriteBatch batchFix;
	private OrthographicCamera cam;
	private Viewport vp;
	private MainEntity me;
	private BitmapFont font;
	private Thread plThread;
	private ShapeRenderer shapes;
	public static NetworkManage networkManage;
	private SceneManage sm;
	private int scene = 0;
	

	@Override
	public void create() {
		sl = new SceneLoader();
		vp = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sl.loadScene("StartScene", vp);

		me = new MainEntity(sl);

		batchFix = new SpriteBatch();
		font = new BitmapFont();
		shapes = new ShapeRenderer();
		font.setColor(Color.RED);

		cam = (OrthographicCamera) vp.getCamera();
		
		// Load API
		PlayerAPI.load(sl, me);
				
		sm = new SceneManage(sl, vp, cam);
		
	}

	public void act() {
		/*
		cam.position.x = myEntity.getX();
		cam.position.y = myEntity.getY();

		if (cam.position.y < Gdx.graphics.getHeight() / 2) {
			cam.position.y = Gdx.graphics.getHeight() / 2;
		}
		if (cam.position.x < Gdx.graphics.getWidth() / 2) {
			cam.position.x = Gdx.graphics.getWidth() / 2;
		}
		*/
	}

	@Override
	public void render() {
		try {
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			act();
			sl.getEngine().update(Gdx.graphics.getDeltaTime());
			sm.render();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			Gdx.app.exit();
		} catch (Exception e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
	}

	@Override
	public void resize(int width, int height) {
		sm.resize(width, height);
	}

	


}
