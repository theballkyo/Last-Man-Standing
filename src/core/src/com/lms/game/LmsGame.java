package com.lms.game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.api.PlayerAPI;
import com.lms.entity.CoreEntity;
import com.lms.entity.MainEntity;
import com.lms.entity.SheepEntity;
import com.lms.network.NetworkEventJoin;
import com.lms.network.NetworkManage;
import com.uwsoft.editor.renderer.SceneLoader;

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
	
	public static float pingTime = 0;
	public static float avgPingTime = 0;
	public static float sumPingTime = 0;
	public static int countPing = 0;
	@Override
	public void create() {
		sl = new SceneLoader();
		vp = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sl.loadScene("MainScene", vp);
		
		me = new MainEntity(sl);
		
		batchFix = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
		
		//Load API
		PlayerAPI.load(sl, me);
		
		
		PlayerAPI.add(LmsConfig.playerName, "sheep", 100f, 50f);
		myEntity = PlayerAPI.get(LmsConfig.playerName);
		myEntity.addScript(new Player(sl.world));
		PlayerAPI.setScale(LmsConfig.playerName, 1.5f);
		cam = (OrthographicCamera) vp.getCamera();
		
		// Connect to server
		final NetworkManage UDPConn = new NetworkManage(new UDPClient(LmsConfig.host, LmsConfig.port), sl, me, vp);
		// final NetworkManage TCPConn = new NetworkManage(new TCPClient(LmsConfig.host, LmsConfig.port), sl, me, vp);
		Thread nmThread= new Thread(UDPConn);
		nmThread.start();
		
		UDPConn.sendJoin(myEntity.getName(), myEntity.getType(), myEntity.getX(), myEntity.getY());
		
		UDPConn.rqList();
		
		plThread = new Thread(new Runnable() {
			public void run() {
				while(true) {
					UDPConn.sendMove(myEntity.getName(), myEntity.getX(), myEntity.getY());	
					
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		
		plThread.start();
		
		while(!NetworkEventJoin.isJoin) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			UDPConn.sendJoin(myEntity.getName(), myEntity.getType(), myEntity.getX(), myEntity.getY());
		}
		
		// Request player list
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					UDPConn.rqList();
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		// Ping
		new Thread(new Runnable() {
			public void run() {
				while(true) {
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

	public void act() {

		cam.position.x = myEntity.getX();
		cam.position.y = myEntity.getY();

		if (cam.position.y < Gdx.graphics.getHeight() / 2)
			cam.position.y = Gdx.graphics.getHeight() / 2;
		if (cam.position.x < Gdx.graphics.getWidth() / 2)
			cam.position.x = Gdx.graphics.getWidth() / 2;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		act();
		sl.getEngine().update(Gdx.graphics.getDeltaTime());
		batchFix.begin();
		font.draw(batchFix, String.format("Ping %.2f ms. | avg %.2f ms.", pingTime, avgPingTime), 5, Gdx.graphics.getHeight()-5);
		font.draw(batchFix, String.format("Player position %.0f:%.0f", myEntity.getX(), myEntity.getY()), 5, Gdx.graphics.getHeight()-25);
		batchFix.end();
	}

	public void resize(int width, int height) {
		sl.rayHandler.useCustomViewport(vp.getScreenX(), vp.getScreenY(), width, height);
		cam = (OrthographicCamera) vp.getCamera();
		Gdx.app.log("Cam", "" + cam.viewportHeight + " " + height + " " + (cam.viewportHeight - height));
		cam.position.y -= Math.abs(cam.viewportHeight - height);
		cam.position.x -= Math.abs(cam.viewportWidth - width);
		cam.viewportWidth = width;
		cam.viewportHeight = height;

		if (cam.position.y < height / 2)
			cam.position.y = height / 2;
		if (cam.position.x < width / 2)
			cam.position.x = width / 2;
	}
	
	private void ping() {
		avgPingTime = sumPingTime / countPing;
		countPing = 0;
		sumPingTime = 0;
	}
}
