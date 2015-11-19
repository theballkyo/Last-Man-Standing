package com.lms.buff;

import com.lms.api.PlayerAPI;

public class GodBuff extends Buff {

	public GodBuff(String playerName, int duration) {
		super(playerName, duration);
	}

	@Override
	public void init() {
		System.out.println("God buff: " + duration);
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
