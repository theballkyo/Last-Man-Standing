package com.lms.network;

import java.net.DatagramPacket;

import com.lms.api.PlayerAPI;
import com.lms.api.PlayerServerAPI;

public class NetworkEventMove extends NetworkEvent {

	public static final byte headerCode = 0x04;
	
	public NetworkEventMove(NetworkManage nm, NetworkServerAbstract ns) {
		super(nm, ns);
	}
	
	@Override
	public byte headerCode() {
		return 0;
	}

	/**
	 * Data rule
	 * NAME:X:Y:ANIMATION
	 */
	@Override
	public void process(String data) {
		String[] dat = data.split(":");
		PlayerAPI.move(dat[0], Float.parseFloat(dat[1]), Float.parseFloat(dat[2]));
	}
	
	@Override
	public void processServer(String data, DatagramPacket incoming, String time) {
		String[] dat = data.split(":");
		PlayerServerAPI.update(dat[0], Float.parseFloat(dat[1]), Float.parseFloat(dat[2]));
		ns.broadcast(dat[0], createMoveMsg(dat[0], Float.parseFloat(dat[1]), Float.parseFloat(dat[2])), time);
	}

	public static String createMoveMsg(String name, float x, float y) {
		return String.format("%c%s:%.0f:%.0f", headerCode, name, x, y);
	}
}