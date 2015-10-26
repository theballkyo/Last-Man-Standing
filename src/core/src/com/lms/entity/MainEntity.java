package com.lms.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.uwsoft.editor.renderer.SceneLoader;

public class MainEntity {
	
	SceneLoader sl;
	
	Map<String, Class<?>> ret = new HashMap<String, Class<?>>();
	
	public MainEntity(SceneLoader sl) {
		this.sl = sl;
		
		ret.put("sheep", SheepEntity.class);
	}
	
	public <T extends CoreEntity> T create(String type, String name) {
		
		try {
			return (T) ret.get(type).getDeclaredConstructor(String.class, SceneLoader.class).newInstance(name, sl);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
