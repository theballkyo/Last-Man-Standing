package com.lms.buff;

import com.lms.api.PlayerAPI;

public class GodBuff extends Buff {

	public GodBuff(String playerName, long duration) {
		super(playerName, duration);
	}

	@Override
	public void init() {
		PlayerAPI.get(playerName).setGodMode(3000);
	}

	@Override
	public void timeout() {

	}

	@Override
	public String[] getArg() {
		return new String[] { String.valueOf(duration) };
	}

}
