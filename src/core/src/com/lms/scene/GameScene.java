package com.lms.scene;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.buff.CoreBuff;
import com.lms.buff.GodBuff;
import com.lms.entity.CoreEntity;
import com.lms.entity.EntityData;
import com.lms.entity.MainEntity;
import com.lms.game.LmsConfig;
import com.lms.game.LmsGame;
import com.lms.game.LmsSound;
import com.lms.item.SpeedUpItem;
import com.lms.network.NetworkEventJoin;
import com.lms.network.NetworkManage;
import com.lms.network.NetworkPing;
import com.lms.object.CoreObject;
import com.lms.object.ItemObject;
import com.lms.scene.SceneManage.SceneName;
import com.lms.script.BulletScript;
import com.lms.script.Player;
import com.lms.script.SwordScript;
import com.uwsoft.editor.renderer.SceneLoader;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class GameScene extends Scene {

	private CoreEntity myEntity;
	private PlayerData myPlayerData;
	private ShapeRenderer shapes;
	private BitmapFont font;
	private SpriteBatch batchFix;

	private Thread sendMoveThread;

	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;

	private boolean startPro = false;

	public GameScene(SceneLoader sl, Viewport vp, OrthographicCamera cam, SceneManage sm) {
		super(sl, vp, cam, sm);
	}

	@Override
	public void create() {
		font = new BitmapFont();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/test.otf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		parameter.color = Color.WHITE;
		font = generator.generateFont(parameter);
		shapes = new ShapeRenderer();
		batchFix = new SpriteBatch();
		font.setColor(Color.BLACK);

		sl.loadScene("test", vp);

		sendMoveThread = new Thread(() -> {
			while (true) {
				// 
				try {
					Thread.sleep(12);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		PlayerAPI.removeAll();
		PlayerAPI.add(LmsConfig.playerName, LmsConfig.playerType, 800, 1000);

		for (Entity e : sl.getEngine().getEntities()) {
			EntityData.addEntity(e);
		}
		myPlayerData = PlayerAPI.get(LmsConfig.playerName);
		myEntity = PlayerAPI.get(LmsConfig.playerName).getCoreEntity();
		myEntity.addScript(new Player(sl.world, 12200f, true));
		if (!connToServer()) {
			sm.setScene(SceneName.StartScene);
		}

		myEntity.addScript(new BulletScript(1, sl));
		myEntity.addScript(new SwordScript());
		// CoreBuff.add(LmsConfig.playerName, new
		// SpeedBuff(LmsConfig.playerName, 2000, 300));
		CoreBuff.add(LmsConfig.playerName, new GodBuff(LmsConfig.playerName, 2000));

		// ItemObject.add(new SpeedUpItem(3000, new Vector2(200, 200)));
		LmsSound.playGameSound();
		// sound.play(0.8f);

		if (LmsConfig.playerName.equals("GOD_GM")) {
			
		}

	}

	@Override
	public void render() {
		int x = Gdx.input.getX();
		int y = Gdx.graphics.getHeight() - Gdx.input.getY();
		LmsGame.networkManage.sendMove(myEntity.getName(), myPlayerData.getMoveSeq(), myEntity.getX(), myEntity.getY());
		updatePlayer();
		CoreBuff.update();
		act();

		CoreObject.draw(Gdx.graphics.getDeltaTime(), sl.getBatch(), sl);
		if (LmsConfig.debug) {
			debug();
		}
		leaderboard();
		if (!LmsGame.networkManage.isConn()) {
			sm.setScene(SceneName.StartScene);
		}

		if (Gdx.input.isKeyJustPressed(Keys.P)) {
			// startPro = !startPro;
		}

		if (startPro) {
			myEntity.setX(x);
			myEntity.setY(y);
		}

		// Set a game title
		Gdx.graphics.setTitle(String.format("%s %s - %s %d kills %d dead", LmsConfig.title, LmsConfig.version, myPlayerData.getName(),
				myPlayerData.getKill(), myPlayerData.getDead()));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void dispose() {
		LmsSound.stopGameSound();
		try {
			LmsGame.networkManage.stop();
			sendMoveThread.stop();
		} catch (NullPointerException e) {

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
		NetworkEventJoin.udpJoin = false;
		NetworkEventJoin.tcpJoin = false;
		do {
			LmsGame.networkManage.sendJoin(myEntity.getName(), myEntity.getType(), myEntity.getX(), myEntity.getY());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tryConnTime += 1;

			if (tryConnTime >= 10 || !LmsGame.networkManage.isConn()) {
				Gdx.app.error("Connection", "No network connect");
				return false;
			}
		} while (!NetworkEventJoin.tcpJoin || !NetworkEventJoin.udpJoin);

		if (LmsConfig.errorCode != 0) {
			return false;
		}

		LmsGame.networkManage.updateList();

		sendMoveThread.start();

		LmsGame.networkManage.reqBuff();
		return true;

	}

	private void updatePlayer() {
		for (Entry<String, PlayerData> p : PlayerAPI.getAll().entrySet()) {
			if (p.getKey().equals(LmsConfig.playerName)) {
				p.getValue().pos.x = myEntity.getX();
				p.getValue().pos.y = myEntity.getY();
				continue;
			}
			p.getValue().updateEntity();
		}
	}

	public void act() {

		cam.position.x = myEntity.getX() + myEntity.getWidth() / 2;
		cam.position.y = myEntity.getY() + myEntity.getHeight() / 2;

		if (cam.position.y < 340) {
			cam.position.y = 340;
		}
		if (cam.position.y > 820) {
			cam.position.y = 820;
		}
		if (cam.position.x < -9200) {
			cam.position.x = -9200;
		}
		if (cam.position.x > 12000) {
			cam.position.x = 12000;
		}

	}

	private void leaderboard() {

		int m = 1;

		for (Entry<String, PlayerData> p : PlayerAPI.getAll().entrySet()) {
			if (p.getValue().getCoreEntity().scene.equals(sl.getSceneVO().sceneName)) {
				PlayerData pl = p.getValue();

				sl.getBatch().begin();
				font.draw(sl.getBatch(), pl.getName(), pl.pos.x + 150, pl.pos.y - 5);
				sl.getBatch().end();

				batchFix.begin();
				float kd = pl.getKill();
				if (pl.getDead() != 0) {
					kd = pl.getKill() / (float) pl.getDead();
				}

				font.draw(batchFix, String.format("%s %d/%d K/D %.2f", pl.getName(), pl.getKill(), pl.getDead(), kd),
						Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - (5 + (m * 20)));
				batchFix.end();
				m += 1;
			}
		}
	}

	private void debug() {
		DecimalFormat numFormat = new DecimalFormat("###,###,###,###");
		batchFix.begin();
		font.setColor(0, 0, 0, 1);
		font.draw(
				batchFix, "Game version " + LmsConfig.version + " || " + NetworkPing.getString()
						+ " : Total bytes recev " + numFormat.format(NetworkManage.getByteRecv()),
				5, Gdx.graphics.getHeight() - 5);
		font.draw(batchFix, String.format("Welcome %s, Player position %.0f:%.0f ", LmsConfig.playerName,
				myEntity.getX(), myEntity.getY()), 5, Gdx.graphics.getHeight() - 25);
		batchFix.end();

		int m = 1;
		shapes.setProjectionMatrix(sl.getBatch().getProjectionMatrix());

		Iterator<Entry<Long, EntityData>> allEntityData = EntityData.getAll().entrySet().iterator();
		while (allEntityData.hasNext()) {
			EntityData ed = allEntityData.next().getValue();
			if (!ed.isPhysic()) {
				continue;
			}
			shapes.begin(ShapeType.Line);
			shapes.setColor(1, 0, 0, 1);

			shapes.rect(ed.getX(), ed.getY(), ed.getWidth(), ed.getHeight());
			shapes.end();
		}

		for (Entry<String, PlayerData> p : PlayerAPI.getAll().entrySet()) {
			if (p.getValue().getCoreEntity().scene.equals(sl.getSceneVO().sceneName)) {
				PlayerData pl = p.getValue();
				sl.getBatch().begin();

				font.draw(sl.getBatch(), pl.getName(), pl.pos.x + 150, pl.pos.y - 5);
				sl.getBatch().end();
				batchFix.begin();
				font.draw(batchFix, String.format("%s Position %.0f:%.0f | %d kills", pl.getName(), pl.pos.x, pl.pos.y,
						pl.getKill()), 5, Gdx.graphics.getHeight() - (25 + (m * 20)));
				batchFix.end();
				m += 1;
				Vector2 v = new Vector2(pl.pos.x, pl.pos.y);
				// new Rectangle(v.x, v.y, pl.getCoreEntity().getWidth(),
				// pl.getCoreEntity().getHeight());
				pl.getRect();
				shapes.begin(ShapeType.Line);
				shapes.setColor(1, 0, 0, 1);
				// shapes.rect(r.x, r.y, r.width, r.height);
				shapes.polygon(pl.getPolygon().getVertices());
				shapes.end();
			}

		}
	}
}
