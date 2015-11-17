package com.lms.buff;

public class JumpBuff extends Buff {

	private int speed;

	public JumpBuff(String playerName, int duration, int speed) {
		super(playerName, duration);
		this.speed = speed;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void timeout() {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getArg() {
		return new String[] { String.valueOf(speed) };
	}
}
