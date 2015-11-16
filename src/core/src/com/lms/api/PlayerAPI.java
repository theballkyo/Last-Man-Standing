package com.lms.api;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.lms.buff.CoreBuff;
import com.lms.buff.GodBuff;
import com.lms.entity.CoreEntity;
import com.lms.entity.MainEntity;
import com.lms.game.LmsConfig;
import com.lms.game.LmsConfig.GameType;
import com.uwsoft.editor.renderer.SceneLoader;

public class PlayerAPI {

	private static HashMap<String, CoreEntity> playerList = new HashMap<String, CoreEntity>();

	private static HashMap<String, PlayerData> players = new HashMap<String, PlayerData>();

	public static SceneLoader sl;
	public static MainEntity me;

	public static void load(SceneLoader sl2, MainEntity me2) {
		sl = sl2;
		me = me2;
	}

	public static void add(String name, String type, float x, float y) {
		PlayerData pd = players.get(name);

		if (pd != null) {
			return;
		}

		pd = new PlayerData(name, type, x, y);
		players.put(name, pd);

		if (LmsConfig.gameType == GameType.Client) {
			try {
				CoreEntity ce = me.newEntity(type, name);
				ce.create();
				pd.setCoreEntity(ce);
			} catch (NullPointerException e) {
				Gdx.app.log("PlayerAPI", "Can't find entity type (" + type + ")");
				e.printStackTrace();
			}
		}
		/*
		 * CoreEntity pl = playerList.get(name);
		 *
		 * if (pl != null) { if (name.equals(LmsConfig.playerName)) { return; }
		 * move(name, x, y); return; }
		 *
		 * pl = me.newEntity(type, name); if (pl == null) {
		 * Gdx.app.log("PlayerAPI", "Can't find entity type (" + type + ")");
		 * return; } pl.create(); pl.setX(x); pl.setY(y); pl.setAnimation(true);
		 * System.out.printf("Create: %s %s %.0f %.0f\n", name, type, x, y);
		 * playerList.put(name, pl);
		 */
	}

	public static void move(String name, float x, float y) {
		PlayerData pd = players.get(name);

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

		/*
		 * CoreEntity pl = playerList.get(name); if (pl == null) { Gdx.app.log(
		 * "PlayerAPI - Move", "Can't find entity name (" + name + ")"); return;
		 * } if (pl.getX() > x) { pl.setScaleX(-Math.abs(pl.getScaleX())); }
		 * else if (pl.getX() < x) { pl.setScaleX(Math.abs(pl.getScaleX())); }
		 *
		 * pl.setX(x); pl.setY(y);
		 */
		// pd.updateEntity();
	}

	public static void setAnimation(String name, boolean play) {

		// Check if Server access
		if (LmsConfig.gameType == GameType.Server) {
			return;
		}

		try {
			players.get(name).getCoreEntity().setAnimation(play);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		/*
		 * CoreEntity pl = playerList.get(name); if (pl == null) {
		 * Gdx.app.log("PlayerAPI", "Can't find entity name (" + name + ")");
		 * return; } pl.setAnimation(play);
		 */
	}

	public static PlayerData get(String name) {
		return players.get(name);
	}

	public static PlayerData get(long id) {
		for (Entry<String, PlayerData> p : players.entrySet()) {
			if (p.getValue().getId() != -1 && p.getValue().getId() == id) {
				return p.getValue();
			}
		}
		return null;
	}

	public static void remove(String name) {
		try {
			sl.getEngine().removeEntity(players.get(name).getCoreEntity().getEntity());
			players.remove(name);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void removeScript(String name) {
		players.get(name).getCoreEntity().removeScript();
	}

	public static HashMap<String, PlayerData> getAll() {
		return players;
	}

	public static void setScale(String name, float f) {
		CoreEntity pl = playerList.get(name);
		if (pl == null) {
			return;
		}
		pl.setScaleX(f);
		pl.setScaleY(f);
	}

	public static void removeAll() {
		for (Entry<String, PlayerData> p : players.entrySet()) {
			System.out.println("Remove: " + p.getKey());
			remove(p.getKey());
		}
		players.clear();
	}

	public static void dead(String name) {
		PlayerData pd = players.get(name);

		if (pd == null || pd.isGod()) {
			return;
		}

		pd.pos.x = 100;
		pd.pos.y = 300;

		CoreBuff.add(name, new GodBuff(name, 3000));
		pd.updateEntity();

	}

	public static void addKill(String name) {
		PlayerData p = players.get(name);
		if (p == null) {
			return;
		}
		p.addKill();
	}

	public static void setKill(String name, int kill) {
		PlayerData p = players.get(name);
		if (p == null) {
			return;
		}
		p.setKill(kill);
	}
}
