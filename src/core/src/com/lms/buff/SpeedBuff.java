package com.lms.buff;

import java.util.HashMap;

import com.lms.api.PlayerAPI;

public class SpeedBuff extends Buff {

	private static int speed;

	private static HashMap<String, Integer> count = new HashMap<>();

	private static boolean isBuff = false;
	public SpeedBuff(String playerName, int duration, int speed) {
		super(playerName, duration);
		
		buffCode = 0x00;
		
		if (!isBuff)
			SpeedBuff.speed = speed;
	}

	@Override
	public void init() {
		if (isBuff)
			return;
		isBuff = true;
		PlayerAPI.get(playerName).speed.x += speed;
	}

	@Override
	public void timeout() {
		if (!isBuff)
			return;
		isBuff = false;
		System.out.println("Speed buff timeout: " + speed);
		PlayerAPI.get(playerName).speed.x = 700;
	}

	@Override
	public String[] getArg() {
		return new String[] { String.valueOf(speed) };
	}

}
