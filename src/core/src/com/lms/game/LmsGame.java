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
import com.lms.entity.CoreEntity;
import com.lms.entity.MainEntity;
import com.lms.network.NetworkEventJoin;
import com.lms.network.NetworkManage;
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
	private CoreEntity myEntity;
	private BitmapFont font;
	private Thread plThread;
	private ShapeRenderer shapes;

	private int scene = 0;
	public static float pingTime = 0;
	public static float avgPingTime = 0;
	public static float sumPingTime = 0;
	public static int countPing = 0;

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

		// Load API
		PlayerAPI.load(sl, me);

		sl.addComponentsByTagName("button", ButtonComponent.class);

		sl.entityFactory.getEntityByUniqueId(40).getComponent(ButtonComponent.class).addListener(new ButtonListener() {

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
				System.out.println("Clicked !");
				scene = 1;
			}
		});

		PlayerAPI.add(LmsConfig.playerName, "figther", 100f, 50f);
		myEntity = PlayerAPI.get(LmsConfig.playerName);
		myEntity.addScript(new Player(sl.world));
		// PlayerAPI.setScale(LmsConfig.playerName, 1.5f);
		cam = (OrthographicCamera) vp.getCamera();

	}

	public void act() {

		cam.position.x = myEntity.getX();
		cam.position.y = myEntity.getY();

		if (cam.position.y < Gdx.graphics.getHeight() / 2) {
			cam.position.y = Gdx.graphics.getHeight() / 2;
		}
		if (cam.position.x < Gdx.graphics.getWidth() / 2) {
			cam.position.x = Gdx.graphics.getWidth() / 2;
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		act();
		sl.getEngine().update(Gdx.graphics.getDeltaTime());

		if (scene == 1 && !sl.getSceneVO().sceneName.equals("MainScene")) {
			PlayerAPI.remove(LmsConfig.playerName);
			sl.engine.removeAllEntities();
			sl.loadScene("MainScene", vp);

			connToServer();

			PlayerAPI.add(LmsConfig.playerName, "figther", 100f, 50f);
			myEntity = PlayerAPI.get(LmsConfig.playerName);
			myEntity.addScript(new Player(sl.world));
		}

		batchFix.begin();
		font.draw(batchFix, String.format("Ping %.2f ms. | avg %.2f ms.", pingTime, avgPingTime), 5,
				Gdx.graphics.getHeight() - 5);
		font.draw(batchFix, String.format("Player position %.0f:%.0f", myEntity.getX(), myEntity.getY()), 5,
				Gdx.graphics.getHeight() - 25);
		font.draw(batchFix, String.format("Welcome %s", LmsConfig.playerName), 5, Gdx.graphics.getHeight() - 45);
		int m = 1;

		shapes.setProjectionMatrix(sl.getBatch().getProjectionMatrix());
		shapes.begin(ShapeType.Line);
		shapes.setColor(1, 0, 0, 1);
		for (Entry<String, CoreEntity> p : PlayerAPI.getAll().entrySet()) {
			if (p.getValue().scene.equals(sl.getSceneVO().sceneName)) {
				CoreEntity pl = p.getValue();
				font.draw(batchFix, String.format("%s Position %.0f:%.0f", pl.getName(), pl.getX(), pl.getY()), 5,
						Gdx.graphics.getHeight() - (45 + (m * 20)));
				m += 1;
				Vector2 v = new Vector2(pl.getX(), pl.getY());
				Rectangle r = new Rectangle(v.x, v.y, pl.getWidth(), pl.getHeight());

				shapes.identity();
				shapes.translate(pl.getX(), -pl.getX(), 0.f);

				shapes.rotate(0.f, 0.f, 1.f, 45.f);

				shapes.rect(pl.getX(), pl.getY(), 100, 200);

			}

		}
		batchFix.end();

		shapes.end();

		if (sl.getSceneVO().sceneName.equals("MainScene")) {
			TransformComponent tc = sl.entityFactory.getEntityByUniqueId(44).getComponent(TransformComponent.class);
			DimensionsComponent dc = myEntity.getEntity().getComponent(DimensionsComponent.class);
			Rectangle r = new Rectangle(myEntity.getX(), myEntity.getY(), dc.width, dc.height);
			Vector2 v = new Vector2();
			r.getCenter(v);
			tc.x = v.x;
			tc.y = v.y;

		}
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

	private void ping() {
		avgPingTime = sumPingTime / countPing;
		countPing = 0;
		sumPingTime = 0;
	}

	private void connToServer() {
		// Connect to server
		final NetworkManage networkManage = new NetworkManage(new UDPClient(LmsConfig.host, LmsConfig.UDPport),
				new TCPClient(LmsConfig.host, LmsConfig.TCPport), sl, me, vp);

		Thread nmThread = new Thread(networkManage);
		nmThread.start();
		
		try {
			nmThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		plThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					networkManage.sendMove(myEntity.getName(), myEntity.getX(), myEntity.getY());

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		});

		plThread.start();

		do {
			networkManage.sendJoin(myEntity.getName(), myEntity.getType(), myEntity.getX(), myEntity.getY());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!NetworkEventJoin.isJoin);

		// Ping
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					ping();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
