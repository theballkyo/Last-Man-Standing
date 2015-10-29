package com.lms.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import net.lastman.network.core.UDPClient;

public class LmsGame extends ApplicationAdapter {
	
	private SceneLoader sl;
	private OrthographicCamera cam;
	private Viewport vp;
	private MainEntity me;
	private CoreEntity myEntity;

	private Thread plThread;
	
	@Override
	public void create() {
		sl = new SceneLoader();
		vp = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sl.loadScene("MainScene", vp);
		
		me = new MainEntity(sl);
		
		//Load API
		PlayerAPI.load(sl, me);
		
		PlayerAPI.add(LmsConfig.playerName, "sheep", 100f, 50f);
		myEntity = PlayerAPI.get(LmsConfig.playerName);
		myEntity.addScript(new Player(sl.world));
		
		cam = (OrthographicCamera) vp.getCamera();
		
		// Connect to server
		final NetworkManage nm = new NetworkManage(new UDPClient(LmsConfig.host, LmsConfig.port), sl, me, vp);
		Thread nmThread= new Thread(nm);
		nmThread.start();
		
		nm.sendJoin(myEntity.getName(), myEntity.getType(), myEntity.getX(), myEntity.getY());
		
		nm.rqList();
		
		plThread = new Thread(new Runnable() {
			public void run() {
				while(true) {
					nm.sendMove(myEntity.getName(), myEntity.getX(), myEntity.getY());	
					
					try {
						Thread.sleep(5);
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
			nm.sendJoin(myEntity.getName(), myEntity.getType(), myEntity.getX(), myEntity.getY());
		}
		
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					nm.rqList();
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
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
}
