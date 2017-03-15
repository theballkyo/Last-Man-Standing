package com.lms.buff;

public abstract class Buff {

	private long startTime;

	protected int duration;

	protected String playerName;

	protected byte buffCode;

	public Buff(String playerName, int duration) {
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

	public int timeRemain() {
		return (int) (System.currentTimeMillis() + duration - startTime);
	}

	public boolean isTimeout() {
		return System.currentTimeMillis() - duration > startTime;
	}

	public byte getBuffCode() {
		return buffCode;
	}

	abstract public String[] getArg();
}
