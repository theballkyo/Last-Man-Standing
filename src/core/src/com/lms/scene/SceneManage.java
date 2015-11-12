package com.lms.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;

public class SceneManage {

	private SceneLoader sl;
	private Viewport vp;
	private OrthographicCamera cam;
	
	private SceneName sn;
	
	private Scene scene;
	
	public enum SceneName {
		StartScene,
		PlayScene
	}
	
	public SceneManage(SceneLoader sl, Viewport vp, OrthographicCamera cam) {
		this.sl = sl;
		this.vp = vp;
		this.cam = cam;
		
		this.sn = SceneName.StartScene;
		
		scene = new StartScene(sl, vp, cam, this);
		scene.create();
	}
	
	public void render() {
		scene.render();
	}
	
	public void resize(int width, int height) {
		scene.resize(width, height);
	}
	
	public void setScene(SceneName sn) {
		scene.dispose();
		switch(sn) {
			case StartScene:
				scene = new StartScene(sl, vp, cam, this);
				break;
			case PlayScene:
				scene = new GameScene(sl, vp, cam, this);
				break;
		}
		scene.create();
	}
}
