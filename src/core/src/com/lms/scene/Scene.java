package com.lms.scene;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;

public abstract class Scene extends ApplicationAdapter {

	SceneLoader sl;
	Viewport vp;
	OrthographicCamera cam;
	SceneManage sm;

	public Scene(SceneLoader sl, Viewport vp, OrthographicCamera cam, SceneManage sm) {
		this.sl = sl;
		this.vp = vp;
		this.cam = cam;
		this.sm = sm;
	}

}
