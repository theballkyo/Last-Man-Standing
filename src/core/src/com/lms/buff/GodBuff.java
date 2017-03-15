package com.lms.buff;

import com.lms.api.PlayerAPI;
import com.lms.game.LmsConfig;

public class GodBuff extends Buff {

	public GodBuff(String playerName, int duration) {
		super(playerName, duration);
	}

	@Override
	public void init() {
		if (LmsConfig.debug) {
			System.out.println("GET God buff: " + duration);
		}
		PlayerAPI.get(playerName).setGodMode(duration);
	}

	@Override
	public void timeout() {

	}

	@Override
	public String[] getArg() {
		return new String[] {};
	}

}
