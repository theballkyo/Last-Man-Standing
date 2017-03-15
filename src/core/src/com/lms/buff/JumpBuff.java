package com.lms.buff;

import java.util.HashMap;

import com.lms.api.PlayerAPI;
import com.lms.game.LmsConfig;

public class JumpBuff extends Buff {

	private static int speed;

	private static HashMap<String, Integer> count = new HashMap<>();

	private static boolean isBuff = false;

	public JumpBuff(String playerName, int duration, int speed) {
		super(playerName, duration);

		buffCode = 0x00;

		if (!JumpBuff.isBuff) {
			JumpBuff.speed = speed;
		}
	}

	@Override
	public void init() {
		if (LmsConfig.debug) {
			System.out.println("GET Jumpbuff: " + speed);
		}
		if (JumpBuff.isBuff) {
			return;
		}
		JumpBuff.isBuff = true;
		PlayerAPI.get(playerName).speedJump += JumpBuff.speed;
	}

	@Override
	public void timeout() {
		if (!JumpBuff.isBuff) {
			return;
		}
		if (LmsConfig.debug) {
			System.out.println("Jumpbuff timeout");
		}
		JumpBuff.isBuff = false;

		PlayerAPI.get(playerName).speedJump = 1000f;
	}

	@Override
	public String[] getArg() {
		return new String[] { String.valueOf(JumpBuff.speed) };
	}

}
