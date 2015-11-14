package com.lms.buff;

public abstract class Buff {

	private long startTime;
	
	protected long duration;

	protected String playerName;
	
	protected byte buffCode;
	
	public Buff(String playerName, long duration) {
		this.playerName = playerName;
		this.duration = duration;
		startTime = System.currentTimeMillis();
	}

	abstract public void init();
	abstract public void timeout();
	
	public long getDuration() {
		return duration;
	};

	public long getStartTime() {
		return startTime;
	}
	
	public long timeRemain() {
		return System.currentTimeMillis() + duration - startTime;
	}
	
	public boolean isTimeout() {
		return System.currentTimeMillis() - duration > startTime; 
	}
	
	public byte getBuffCode() {
		return buffCode;
	}
}
