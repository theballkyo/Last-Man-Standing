package com.lms.buff;

import java.util.HashMap;

import com.lms.api.PlayerAPI;
import com.lms.game.LmsConfig;

public class SpeedBuff extends Buff {

	private static int speed;

	private static HashMap<String, Integer> count = new HashMap<>();

	private static boolean isBuff = false;

	public SpeedBuff(String playerName, int duration, int speed) {
		super(playerName, duration);

		buffCode = 0x00;

		if (!SpeedBuff.isBuff) {
			SpeedBuff.speed = speed;
		}
	}

	@Override
	public void init() {
		if (LmsConfig.debug) {
			System.out.println("GET Speedbuff: " + speed);
		}
		if (SpeedBuff.isBuff) {
			return;
		}
		SpeedBuff.isBuff = true;
		PlayerAPI.get(playerName).speed.x += SpeedBuff.speed;
	}

	@Override
	public void timeout() {
		if (!SpeedBuff.isBuff) {
			return;
		}
		if (LmsConfig.debug) {
			System.out.println("Speedbuff timeout");
		}
		SpeedBuff.isBuff = false;
		PlayerAPI.get(playerName).speed.x = 1100;
	}

	@Override
	public String[] getArg() {
		return new String[] { String.valueOf(SpeedBuff.speed) };
	}

}
