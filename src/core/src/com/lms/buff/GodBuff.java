package com.lms.buff;

import com.lms.api.PlayerAPI;

public class GodBuff extends Buff {

	public GodBuff(String playerName, long duration) {
		super(playerName, duration);
		buffCode = 0x01;
	}

	@Override
	public void init() {
		PlayerAPI.get(playerName).setGodMode(3000);
	}

	@Override
	public void timeout() {

	}

}
