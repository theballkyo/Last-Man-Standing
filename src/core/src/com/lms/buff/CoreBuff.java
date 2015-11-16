package com.lms.buff;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.game.LmsConfig;
import com.lms.game.LmsGame;

public class CoreBuff {

	private static HashMap<String, Byte> buffCode = new HashMap<>();

	static {
		buffCode.put("GodBuff", (byte) 0x00);
		buffCode.put("SpeedBuff", (byte) 0x01);
		buffCode.put("JumpBuff", (byte) 0x02);

	}

	public static void add(String name, Buff buff) {
		PlayerAPI.get(name).addBuff(buff);
		buff.init();
		if (name.equals(LmsConfig.playerName)) {
			LmsGame.networkManage.sendBuff(buffCode.get(buff.getClass().getSimpleName()), name, buff.getArg());
		}
	}

	public static void processBuff(byte buffCode, String name, String[] arg) {
		if (buffCode == 0x00) {
			add(name, new GodBuff(name, Long.parseLong(arg[0])));
		} else if (buffCode == 0x01) {
			add(name, new SpeedBuff(name, Long.parseLong(arg[0]), Integer.parseInt(arg[1])));
		} else if (buffCode == 0x02) {
			add(name, new SpeedBuff(name, Long.parseLong(arg[0]), Integer.parseInt(arg[1])));
		}
	}

	public static void update() {
		for (Entry<String, PlayerData> p : PlayerAPI.getAll().entrySet()) {
			Iterator<Buff> iter = p.getValue().getAllBuff().iterator();
			try {
				while (iter.hasNext()) {
					Buff buff = iter.next();
					if (buff.isTimeout()) {
						System.out.println(p.getKey() + ":" + buff.getClass().getSimpleName() + " timeout");
						buff.timeout();
						iter.remove();
					}
				}
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
				break;
			}
		}
	}

}
