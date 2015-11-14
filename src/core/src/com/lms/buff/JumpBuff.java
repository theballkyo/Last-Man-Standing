package com.lms.buff;

public class JumpBuff extends Buff {

	public JumpBuff(String playerName, long duration) {
		super(playerName, duration);
		buffCode = 0x02;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void timeout() {
		// TODO Auto-generated method stub
		
	}

}
