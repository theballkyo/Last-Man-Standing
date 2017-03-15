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
		CoreBuff.buffCode.put("GodBuff", (byte) 0x00);
		CoreBuff.buffCode.put("SpeedBuff", (byte) 0x01);
		CoreBuff.buffCode.put("JumpBuff", (byte) 0x02);

	}

	public static void add(String name, Buff buff) {
		if (name.equals(LmsConfig.playerName)) {
			LmsGame.networkManage.sendBuff(CoreBuff.buffCode.get(buff.getClass().getSimpleName()), name, buff.duration,
					buff.getArg());
		}
	}

	private static void add_(String name, Buff buff) {
		if (PlayerAPI.get(name) == null)
			return;
		PlayerAPI.get(name).addBuff(buff);
	}

	public static void processBuff(byte buffCode, String name, int duration, String[] arg) {
		if (buffCode == 0x00) {
			CoreBuff.add_(name, new GodBuff(name, duration));
		} else if (buffCode == 0x01) {
			CoreBuff.add_(name, new SpeedBuff(name, duration, Integer.parseInt(arg[0])));
		} else if (buffCode == 0x02) {
			CoreBuff.add_(name, new JumpBuff(name, duration, Integer.parseInt(arg[0])));
		}
	}

	public static void update() {
		for (Entry<String, PlayerData> p : PlayerAPI.getAll().entrySet()) {
			Iterator<Buff> iter = p.getValue().getAllBuff().iterator();
			try {
				while (iter.hasNext()) {
					Buff buff = iter.next();
					if (buff.isTimeout()) {
						// System.out.println(p.getKey() + ":" +
						// buff.getClass().getSimpleName() + " timeout");
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
