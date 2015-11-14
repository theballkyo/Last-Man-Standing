package com.lms.buff;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.game.ClassFinder;

public class CoreBuff {

	public static void add(String name, Buff buff) {
		PlayerAPI.get(name).addBuff(buff);
		buff.init();
		
	}
	
	public static void processBuff(byte buffCode, String[] arg) {
		
	}
	
	public static void update() {
		for(Entry<String, PlayerData> p: PlayerAPI.getAll().entrySet()) {
			Iterator<Buff> iter = p.getValue().getAllBuff().iterator();
			while (iter.hasNext()) {
				Buff buff = iter.next();
				if (buff.isTimeout()) {
					System.out.println("Buff timeout");
					buff.timeout();
					iter.remove();
				}
			}
		}
	}
	
}
