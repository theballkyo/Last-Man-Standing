package com.lms.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.uwsoft.editor.renderer.SceneLoader;

public class MainEntity {

	SceneLoader sl;

	Map<String, Class<?>> ret = new HashMap<String, Class<?>>();

	public MainEntity(SceneLoader sl) {
		this.sl = sl;
		ret.put("knight", KnightEntity.class);
		ret.put("cyborg", CyborgEntity.class);
		ret.put("swat", SwatEntity.class);
		ret.put("ninja", NinjaEntity.class);
	}

	public void add(String type, Class<?> c) {
		ret.put(type, c);
	}

	public CoreEntity newEntity(String type, String name) {

		try {
			return (CoreEntity) ret.get(type).getDeclaredConstructor(String.class, SceneLoader.class).newInstance(name,
					sl);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

}
