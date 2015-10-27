package com.lms.entity;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.MainItemVO;

public abstract class CoreEntity {

	private Entity entity;
	protected SceneLoader sl;

	private String entityName;

	public CoreEntity(String entityName) {
		this.entityName = entityName;
	}

	public void add() {
		getScene().engine.addEntity(getEntity());
	}

	public SceneLoader getScene() {
		return this.sl;
	}

	protected void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public String getName() {
		return this.entityName;
	}
}
