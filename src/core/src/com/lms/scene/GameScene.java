package com.lms.scene;

import java.text.DecimalFormat;
import java.util.Map.Entry;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.buff.CoreBuff;
import com.lms.buff.GodBuff;
import com.lms.entity.CoreEntity;
import com.lms.entity.MainEntity;
import com.lms.game.LmsConfig;
import com.lms.game.LmsGame;
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
	private Thread plThread;
	private ShapeRenderer shapes;
	private BitmapFont font;
	private SpriteBatch batchFix;

	private Thread sendMoveThread;
	private Sound sound;
	
	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;
	
	private Random random;

	private boolean isPro = false;
	private boolean startPro = false;
	public GameScene(SceneLoader sl, Viewport vp, OrthographicCamera cam, SceneManage sm) {
		super(sl, vp, cam, sm);
	}

	@Override
	public void create() {
		sound = Gdx.audio.newSound(Gdx.files.internal("sounds/fight.mp3"));
		font = new BitmapFont();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/test.otf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		parameter.color = Color.WHITE;
		font = generator.generateFont(parameter);
		shapes = new ShapeRenderer();
		batchFix = new SpriteBatch();
		font.setColor(Color.WHITE);

		sl.loadScene("MainScene", vp);

		sendMoveThread = new Thread(() -> {
			while (true) {
				LmsGame.networkManage.sendMove(myEntity.getName(), myEntity.getX(), myEntity.getY());
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		PlayerAPI.removeAll();
		//int ran_x = random.nextInt(5500)-1800;
		PlayerAPI.add(LmsConfig.playerName, LmsConfig.playerType, 800, 1000);
		myEntity = PlayerAPI.get(LmsConfig.playerName).getCoreEntity();
		myEntity.addScript(new Player(sl.world, 3950f, true));
		if (!connToServer()) {
			sm.setScene(SceneName.StartScene);
		}
		
		myEntity.addScript(new BulletScript(1, sl));
		myEntity.addScript(new SwordScript());
		// CoreBuff.add(LmsConfig.playerName, new
		// SpeedBuff(LmsConfig.playerName, 2000, 300));
		CoreBuff.add(LmsConfig.playerName, new GodBuff(LmsConfig.playerName, 2000));

		ItemObject.add(new SpeedUpItem(3000, new Vector2(200, 200)));
		sound.loop();
		sound.play(0.8f);
		
		if (LmsConfig.playerName.equals("GOD_GM")) {
			isPro = true;
		}
	}

	@Override
	public void render() {
		int x = Gdx.input.getX();
		int y = Gdx.graphics.getHeight() - Gdx.input.getY();
		updatePlayer();
		CoreBuff.update();
		act();

		CoreObject.draw(Gdx.graphics.getDeltaTime(), sl.getBatch(), 5500f, sl);
		if (LmsConfig.debug) {
			
			//debug();
		}
		leaderboard();
		if (!LmsGame.networkManage.isConn()) {
			sm.setScene(SceneName.StartScene);
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.P)) {
			startPro = !startPro;
		}
		
		if (startPro) {
			myEntity.setX(x);
			myEntity.setY(y);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void dispose() {
		sound.stop();
		sound.dispose();
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

		cam.position.x = myEntity.getX()+myEntity.getWidth()/2;
		cam.position.y = myEntity.getY()+myEntity.getHeight()/2;
		
		if (cam.position.y < 340) {
			cam.position.y = 340;
		}
		if (cam.position.y > 820) {
			cam.position.y = 820;
		}
		if (cam.position.x < - 1460) {
			cam.position.x = -1460;
		}
		if (cam.position.x > 3300) {
			cam.position.x = 3300;
		}
		
	}
	
	private void leaderboard(){
		
		int m = 1;
		
		for (Entry<String, PlayerData> p : PlayerAPI.getAll().entrySet()) {
			if (p.getValue().getCoreEntity().scene.equals(sl.getSceneVO().sceneName)) {
				PlayerData pl = p.getValue();
				
				sl.getBatch().begin();
				font.draw(sl.getBatch(), pl.getName(), pl.pos.x+150, pl.pos.y-5);
				sl.getBatch().end();
				
				batchFix.begin();
				font.draw(batchFix, String.format("%s | %d kills", pl.getName(), 
						pl.getKill()), 600, Gdx.graphics.getHeight() - (5 + (m * 20)));
				batchFix.end();
				m += 1;
			}
		}
	}
	

	private void debug() {
		DecimalFormat numFormat = new DecimalFormat("###,###,###,###");
		batchFix.begin();
		font.draw(batchFix,
				NetworkPing.getString() + " : Total bytes recev " + numFormat.format(NetworkManage.getByteRecv()), 5,
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

				font.draw(sl.getBatch(), pl.getName(), pl.pos.x+150, pl.pos.y-5);
				sl.getBatch().end();
				batchFix.begin();
				font.draw(batchFix, String.format("%s Position %.0f:%.0f | %d kills", pl.getName(), pl.pos.x, pl.pos.y,
						pl.getKill()), 5, Gdx.graphics.getHeight() - (45 + (m * 20)));
				batchFix.end();
				m += 1;
				Vector2 v = new Vector2(pl.pos.x, pl.pos.y);
				Rectangle r = new Rectangle(v.x, v.y, pl.getCoreEntity().getWidth(), pl.getCoreEntity().getHeight());
				r = pl.getRect();
				shapes.begin(ShapeType.Line);
				shapes.setColor(1, 0, 0, 1);
				//shapes.rect(r.x, r.y, r.width, r.height);
				shapes.polygon(pl.getPolygon().getVertices());
				shapes.end();
			}

		}
	}
}
