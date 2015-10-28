package com.lms.entity;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public abstract class CoreEntity {

	protected Entity entity;
	protected SceneLoader sl;

	protected String entityName;

	abstract public void create();
	
	public CoreEntity(String entityName) {
		this.entityName = entityName;
	}

	protected void add() {
		sl.engine.addEntity(entity);
	}

	public void addScript(IScript script) {
		new ItemWrapper(sl.getRoot()).addChild(entity).addScript(script);
	}
	public SceneLoader getScene() {
		return this.sl;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public String getName() {
		return this.entityName;
	}
}
