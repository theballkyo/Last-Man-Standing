package com.lms.api;

import com.lms.network.NetworkEventBuff;

public class BuffData {

	private byte buffCode;
	private int duration;
	private long sTime;
	private String name;
	private String arg;

	public BuffData(byte buffCode, String name, int duration, long sTime, String arg) {
		this.buffCode = buffCode;
		this.name = name;
		this.duration = duration;
		this.sTime = sTime;
		this.arg = arg;
	}

	@Override
	public String toString() {
		System.out.println(System.currentTimeMillis() + ":" + sTime + ":" + duration + ":"
				+ (duration - (System.currentTimeMillis() - sTime)));
		return NetworkEventBuff.createMsg(buffCode, name, (int) (duration - (System.currentTimeMillis() - sTime)), arg);
	}

	public boolean isTimeout() {
		return System.currentTimeMillis() - sTime > duration;
	}
}
