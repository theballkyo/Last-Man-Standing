package com.lms.scene;

import java.text.DecimalFormat;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.entity.CoreEntity;
import com.lms.entity.MainEntity;
import com.lms.game.LmsConfig;
import com.lms.game.LmsGame;
import com.lms.network.NetworkEventJoin;
import com.lms.network.NetworkManage;
import com.lms.network.NetworkPing;
import com.lms.object.BulletObject;
import com.lms.scene.SceneManage.SceneName;
import com.lms.script.BulletScript;
import com.lms.script.Player;
import com.uwsoft.editor.renderer.SceneLoader;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class GameScene extends Scene {

	private CoreEntity myEntity;
	private Thread plThread;
	private ShapeRenderer shapes;
	private BitmapFont font;
	private SpriteBatch batchFix;
	
	public GameScene(SceneLoader sl, Viewport vp, OrthographicCamera cam, SceneManage sm) {
		super(sl, vp, cam, sm);
	}

	public void create() {
		
		font = new BitmapFont();
		shapes = new ShapeRenderer();
		batchFix = new SpriteBatch();
		font.setColor(Color.RED);
		
		sl.loadScene("MainScene", vp);
		
		PlayerAPI.removeAll();
		PlayerAPI.add(LmsConfig.playerName, "ninja", 100f, 50f);
		myEntity = PlayerAPI.get(LmsConfig.playerName).getCoreEntity();
		myEntity.addScript(new Player(sl.world, 1900f));
		myEntity.addScript(new BulletScript(1, sl));
		connToServer();
		
	}
	
	public void render() {
		updatePlayer();
		int m = 1;
		
		act();

		BulletObject.draw(Gdx.graphics.getDeltaTime(), sl.getBatch(), 1900f);
		if (LmsConfig.debug) {
			debug();
		}
		
		if (!LmsGame.networkManage.isConn()) {
			sm.setScene(SceneName.StartScene);
		}
	}
	private boolean connToServer() {
		// Connect to server
		LmsGame.networkManage = new NetworkManage(new UDPClient(LmsConfig.host, LmsConfig.UDPport),
				new TCPClient(LmsConfig.host, LmsConfig.TCPport), sl, new MainEntity(sl), vp);
		Thread nmThread = new Thread(LmsGame.networkManage);
		nmThread.start();

		try {
			nmThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		int tryConnTime = 0;

		do {
			LmsGame.networkManage.sendJoin(myEntity.getName(), myEntity.getType(), myEntity.getX(), myEntity.getY());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tryConnTime += 1;

			if (tryConnTime >= 3 || !LmsGame.networkManage.isConn()) {
				Gdx.app.error("Connection", "No network connect");
				return false;
			}
		} while (!NetworkEventJoin.tcpJoin || !NetworkEventJoin.udpJoin);

		LmsGame.networkManage.updateList();

		new Thread(() -> {
			while(true) {
				LmsGame.networkManage.sendMove(myEntity.getName(), myEntity.getX(), myEntity.getY());
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		return true;
		
	}

	private void updatePlayer() {
		for(Entry<String, PlayerData> p: PlayerAPI.getAll().entrySet()) {
			if (p.getKey().equals(LmsConfig.playerName)) {
				p.getValue().pos.x = myEntity.getX();
				p.getValue().pos.y = myEntity.getY();
				continue;
			}
			p.getValue().updateEntity();
		}
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
		if (myEntity.getX() > 1900 - (Gdx.graphics.getWidth() / 2)) {
			cam.position.x = 1900 - (Gdx.graphics.getWidth() / 2);
		}
		
	}
	
	private void debug() {
		DecimalFormat numFormat = new DecimalFormat("###,###,###,###");
		batchFix.begin();
		font.draw(batchFix, NetworkPing.getString() + " : Total bytes recev " + numFormat.format(NetworkManage.getByteRecv()), 5,
				Gdx.graphics.getHeight() - 5);
		font.draw(batchFix, String.format("Player position %.0f:%.0f", myEntity.getX(), myEntity.getY()), 5,
				Gdx.graphics.getHeight() - 25);
		font.draw(batchFix, String.format("Welcome %s", LmsConfig.playerName), 5, Gdx.graphics.getHeight() - 45);
		batchFix.end();
		
		int m = 1;
		shapes.setProjectionMatrix(sl.getBatch().getProjectionMatrix());
		
		for (Entry<String, PlayerData> p : PlayerAPI.getAll().entrySet()) {
			if (p.getValue().getCoreEntity().scene.equals(sl.getSceneVO().sceneName)) {
				PlayerData pl = p.getValue();
				sl.getBatch().begin();
				font.draw(sl.getBatch(), pl.getName(), pl.pos.x, pl.pos.y);
				sl.getBatch().end();
				batchFix.begin();
				font.draw(batchFix, String.format("%s Position %.0f:%.0f", pl.getName(), pl.pos.x, pl.pos.y), 5,
						Gdx.graphics.getHeight() - (45 + (m * 20)));
				batchFix.end();
				m += 1;
				Vector2 v = new Vector2(pl.pos.x, pl.pos.y);
				Rectangle r = new Rectangle(v.x, v.y, pl.getCoreEntity().getWidth(), pl.getCoreEntity().getHeight());
				shapes.begin(ShapeType.Line);
				shapes.setColor(1, 0, 0, 1);
				shapes.rect(r.x, r.y, r.width, r.height);
				shapes.end();
			}

		} 
	}
}
