package com.lms.game;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.entity.MainEntity;
import com.lms.entity.SheepEntity;
import com.lms.network.NetworkManage;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import net.lastman.network.core.GameClient;
import net.lastman.network.core.UDPClient;

public class LmsGame extends ApplicationAdapter {
	
	private SceneLoader sl;
	private OrthographicCamera cam;
	private Viewport vp;
	private Player player;
	private MainEntity me;
	private GameClient gc;

	private HashMap<String, Entity> clientList;

	@Override
	public void create() {
		sl = new SceneLoader();
		vp = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		sl.loadScene("MainScene", vp);

		me = new MainEntity(sl);

		ItemWrapper root = new ItemWrapper(sl.getRoot());
		player = new Player(sl.world);

		root.getChild("player").addScript(player);
		/*
		 * SheepEntity john = me.create("sheep", "john"); john.vo.x = 400f;
		 * john.create(sl.getRoot()); john.add();
		 * 
		 * SheepEntity carry = me.create("sheep", "carry");
		 * carry.create(sl.getRoot()); carry.add();
		 * 
		 * ItemWrapper root2 = new ItemWrapper(sl.getRoot());
		 * root2.getChild(john.getName()).addScript(new Player(sl.world));
		 * root2.getChild(carry.getName()).addScript(new Player(sl.world));
		 */
		gc = new GameClient();
		while (!gc.run()) {
		}
		gc.sendMsg("J gg1 " + player.getx() + " " + player.gety());

		clientList = new HashMap<String, Entity>();
		clientList.put("gg1", player.getEntity());
		new Thread(new Runnable() {
			public void run() {
				String msg;
				while (true) {
					msg = gc.getInput();
					if (msg == null)
						break;
					if (msg == "P")
						continue;
					if (msg.startsWith("create")) {
						String[] data = msg.split(" ");
						if (data[1].equals("sheep")) {
							SheepEntity ai = me.create("sheep", "AI");
							ai.vo.x = Float.parseFloat(data[2]);
							ai.vo.y = Float.parseFloat(data[3]);
							ai.create(sl.getRoot());
							ai.add();
						}
					}
					if (msg.startsWith("J")) {
						String[] data = msg.split(" ");
						System.out.println(data[1]);
						if (clientList.get(data[1]) != null)
							continue;

						SheepEntity ai = me.create("sheep", "Player" + data[1]);
						ai.vo.x = 250f;
						ai.vo.y = 200f;
						ai.create(sl.getRoot());
						ai.add();
						clientList.put(data[1], ai.getEntity());
					}
					if (msg.startsWith("M")) {
						String[] data = msg.split(" ");

						if (data[1].equals("gg1"))
							continue;
						System.out.println(data[1]);
						Entity e = clientList.get(data[1]);
						TransformComponent tfC = ComponentRetriever.get(e, TransformComponent.class);
						tfC.x = Float.parseFloat(data[2]);
						tfC.y = Float.parseFloat(data[3]);
						tfC.scaleX = Float.parseFloat(data[4]);
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					gc.sendMsg("M gg1 " + player.getx() + " " + player.gety() + " " + player.getScaleX());
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		// Connect to server
		NetworkManage nm = new NetworkManage(new UDPClient(LmsConfig.host, LmsConfig.port));
		Thread nmThread= new Thread(nm);
		nmThread.start();
	}

	public void act() {
		cam = (OrthographicCamera) vp.getCamera();
		// camera.position.x = deer.getCenterX();

		cam.position.x = player.getx();
		// if(Gdx.input.isKeyPressed(Keys.RIGHT)) cam.position.x += 1f;
		cam.position.y = player.gety();

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
