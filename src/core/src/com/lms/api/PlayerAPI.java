package com.lms.api;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.lms.entity.CoreEntity;
import com.lms.entity.MainEntity;
import com.lms.entity.SheepEntity;
import com.uwsoft.editor.renderer.SceneLoader;

public class PlayerAPI {
	
	private static HashMap<String, Object> playerList = new HashMap<String, Object>();;
	
	private static SceneLoader sl;
	private static MainEntity me;
	
	public static void load(SceneLoader sl2, MainEntity me2) {
		sl = sl2;
		me = me2;
	}
	
	public static void add(String name, String type, float x, float y) {
		CoreEntity pl = me.newEntity(type, name);
		if(pl == null) {
			Gdx.app.log("PlayerAPI", "Can't find entity type (" + type + ")");
			return;
		}
		pl.create();
		pl.setX(x);
		pl.setY(y);
		pl.setAnimation(true);
		playerList.put(name, pl);
	}
	
	public static void move(String name, float x, float y) {
		CoreEntity pl = (CoreEntity) playerList.get(name);
		if(pl == null) {
			Gdx.app.log("PlayerAPI", "Can't find entity name (" + name + ")");
			return;
		}
		pl.setX(x);
		pl.setY(y);
	}
	
	public static void setAnimation(String name, boolean play) {
		CoreEntity pl = (CoreEntity) playerList.get(name);
		if(pl == null) {
			Gdx.app.log("PlayerAPI", "Can't find entity name (" + name + ")");
			return;
		}
		pl.setAnimation(play);	
	}
	
	public static CoreEntity get(String name) {
		CoreEntity obj = (CoreEntity) playerList.get(name);
		
		return obj;
	}
	
	public static void remove(String name) {
		playerList.remove(name);
	}
} 
