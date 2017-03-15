package com.lms.api;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.lms.buff.CoreBuff;
import com.lms.buff.GodBuff;
import com.lms.entity.CoreEntity;
import com.lms.entity.MainEntity;
import com.lms.game.LmsConfig;
import com.lms.game.LmsConfig.GameType;
import com.lms.script.PlayerBot;
import com.uwsoft.editor.renderer.SceneLoader;

public class PlayerAPI {

	private static HashMap<String, CoreEntity> playerList = new HashMap<>();

	private static HashMap<String, PlayerData> players = new HashMap<>();

	public static SceneLoader sl;
	public static MainEntity me;

	public static void load(SceneLoader sl2, MainEntity me2) {
		PlayerAPI.sl = sl2;
		PlayerAPI.me = me2;
	}

	
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @param x
	 * @param y
	 */
	public static void add(String name, String type, float x, float y) {
		PlayerData pd = PlayerAPI.players.get(name);

		if (pd != null) {
			return;
		}

		pd = new PlayerData(name, type, x, y);
		PlayerAPI.players.put(name, pd);

		if (LmsConfig.gameType == GameType.Client) {
			try {
				CoreEntity ce = PlayerAPI.me.newEntity(type, name);
				ce.create();
				pd.setCoreEntity(ce);
			} catch (NullPointerException e) {
				Gdx.app.log("PlayerAPI", "Can't find entity type (" + type + ")");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * Add player
	 * Version 2 is add player with playerBot script
	 * 
	 * @param name
	 * @param type
	 * @param x
	 * @param y
	 */
	public static void addVersion2(String name, String type, float x, float y) {
		add(name, type, x, y);
		PlayerData pd = PlayerAPI.players.get(name);
		if (pd == null) {
			Gdx.app.log("PlayerAPI", "Can't find player name " + name);
			return;
		}
		pd.getCoreEntity().addScript(new PlayerBot());
	}
	
	/**
	 * Move player entity to new position
	 * 
	 * @param name
	 * @param x
	 * @param y
	 */
	public static void move(String name, float x, float y) {
		PlayerData pd = PlayerAPI.players.get(name);

		if (pd == null) {
			Gdx.app.log("PlayerAPI - Move", "Can't find entity name (" + name + ")");
			return;
		}

		if (pd.pos.x > x) {
			pd.scale.x = (-Math.abs(pd.scale.x));
		} else if (pd.pos.x < x) {
			pd.scale.x = Math.abs(pd.scale.x);
		}

		pd.pos.x = x;
		pd.pos.y = y;

	}

	/**
	 * Set player entity with animation name
	 * 
	 * @param name
	 * @param play
	 */
	public static void setAnimation(String name, boolean play) {

		// Check if Server access
		if (LmsConfig.gameType == GameType.Server) {
			return;
		}

		try {
			PlayerAPI.players.get(name).getCoreEntity().setAnimation(play);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get PlayerData
	 * 
	 * @param name
	 * @return
	 */
	public static PlayerData get(String name) {
		return PlayerAPI.players.get(name);
	}

	public static PlayerData get(long id) {
		for (Entry<String, PlayerData> p : PlayerAPI.players.entrySet()) {
			if (p.getValue().getId() != -1 && p.getValue().getId() == id) {
				return p.getValue();
			}
		}
		return null;
	}

	/**
	 * Remove player 
	 * 
	 * @param name
	 */
	public static void remove(String name) {
		try {
			if (PlayerAPI.sl.engine
					.getEntity(PlayerAPI.players.get(name).getCoreEntity().getEntity().getId()) != null) {
				PlayerAPI.sl.getEngine().removeEntity(PlayerAPI.players.get(name).getCoreEntity().getEntity());
				PlayerAPI.players.remove(name);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void removeScript(String name) {
		PlayerAPI.players.get(name).getCoreEntity().removeScript();
	}

	public static HashMap<String, PlayerData> getAll() {
		return PlayerAPI.players;
	}

	public static void setScale(String name, float f) {
		CoreEntity pl = PlayerAPI.playerList.get(name);
		if (pl == null) {
			return;
		}
		pl.setScaleX(f);
		pl.setScaleY(f);
	}

	public static void removeAll() {
		for (Entry<String, PlayerData> p : PlayerAPI.players.entrySet()) {
			System.out.println("Remove: " + p.getKey());
			PlayerAPI.remove(p.getKey());
		}
		PlayerAPI.players.clear();
	}

	public static void dead(String name) {
		PlayerData pd = PlayerAPI.players.get(name);

		if ((pd == null || pd.isGod())) {
			if (pd.getCoreEntity().getY() > -30) {
				return;
			}

		}
		pd.pos.x = (new Random().nextFloat() * (12000 + 9000)) - 9000;
		pd.pos.y = 1200;

		CoreBuff.add(name, new GodBuff(name, 1000));
		pd.updateEntity();

	}

	public static void addKill(String name) {
		PlayerData p = PlayerAPI.players.get(name);
		if (p == null) {
			return;
		}
		p.addKill();
	}

	public static void setKill(String name, int kill) {
		PlayerData p = PlayerAPI.players.get(name);
		if (p == null) {
			return;
		}
		p.setKill(kill);
	}
}
