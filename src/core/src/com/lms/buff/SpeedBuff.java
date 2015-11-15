package com.lms.buff;

import com.lms.api.PlayerAPI;

public class SpeedBuff extends Buff{

	private int speed;
	
	public SpeedBuff(String playerName, long duration, int speed) {
		super(playerName, duration);
		this.speed = speed;
		buffCode = 0x00;
	}

	@Override
	public void init() {
		PlayerAPI.get(playerName).speed.x += speed;
	}
	
	@Override
	public void timeout() {
		PlayerAPI.get(playerName).speed.x -= speed;
	}
	
	public String[] getArg() {
		return new String[]{String.valueOf(duration), String.valueOf(speed)};
	}
}
