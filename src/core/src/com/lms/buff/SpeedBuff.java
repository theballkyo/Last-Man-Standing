package com.lms.buff;

import java.util.HashMap;

import com.lms.api.PlayerAPI;

public class SpeedBuff extends Buff {

	private int speed;

	private static HashMap<String, Integer> count = new HashMap<>();

	public SpeedBuff(String playerName, int duration, int speed) {
		super(playerName, duration);
		this.speed = speed;
		buffCode = 0x00;
	}

	@Override
	public void init() {
		count.put(playerName, count.getOrDefault(playerName, 0) + 1);
		if (count.get(playerName) > 1) {
			return;
		}
		PlayerAPI.get(playerName).speed.x += speed;
	}

	@Override
	public void timeout() {
		count.put(playerName, count.getOrDefault(playerName, 1) - 1);
		if (count.get(playerName) >= 1) {
			return;
		}

		PlayerAPI.get(playerName).speed.x -= speed;
	}

	@Override
	public String[] getArg() {
		return new String[] { String.valueOf(speed) };
	}

}
